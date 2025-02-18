package com.project.shopping_app.controllers;

import com.github.javafaker.Faker;
import com.project.shopping_app.dtos.ProductDTO;
import com.project.shopping_app.dtos.ProductImageDTO;
import com.project.shopping_app.model.Product;
import com.project.shopping_app.model.ProductImage;
import com.project.shopping_app.response.ProductListResponse;
import com.project.shopping_app.response.ProductResponse;
import com.project.shopping_app.service.ProductCodeGeneratorService;
import com.project.shopping_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("${api.prefix}/products")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  private final ProductService productService;
  private final ProductCodeGeneratorService productCodeGeneratorService;

  @GetMapping("")
  public ResponseEntity<ProductListResponse> getAllProduct(
        @RequestParam("page") int page,
        @RequestParam("limit") int limit) {
    PageRequest pageRequest = PageRequest.of(
          page, limit,
          Sort.by(Sort.Direction.ASC, "createdAt").descending());
    Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
    // get total page
    int totalPages = productPage.getTotalPages();
    List<ProductResponse> products = productPage.getContent();
    return ResponseEntity.ok(ProductListResponse.builder()
                .products(products)
                .totalProducts(totalPages)
                .build()
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
    try{
      Product product = productService.getProductById(id);
      return ResponseEntity.ok(ProductResponse.fromProduct(product));
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createProduct(
        @Valid @ModelAttribute ProductDTO productDTO,
        BindingResult result) {
    try {
      // check loi validate dau vao
      if (result.hasErrors()) {
        List<String> errorMessage = result.getFieldErrors()
              .stream()
              .map(FieldError::getDefaultMessage)
              .toList();
        return ResponseEntity.badRequest().body(errorMessage);
      }

      // save product vao db
      Product newProduct = productService.createProduct(productDTO);
      log.info("New product created: " + newProduct);

      // check and process list image
      List<MultipartFile> files = productDTO.getFiles();

      // check size file >= 10 mb return false, else oke
//      files = files == null ? new ArrayList<MultipartFile>() : files;

      if (files == null) {
        files = new ArrayList<>();
      }
      if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
        return ResponseEntity.badRequest().body("upload > 5 image cho phep");

      }
//      List<ProductImage> productImages = new ArrayList<>();

      for (MultipartFile file : files) {
        if (file != null) {
          if (file.getSize() == 0) {
            continue;
          }
          if (file.getSize() > 1024 * 1024 * 10) { // size > 10mb
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File to large, Maximum size 10mb");
          }
          String contentType = file.getContentType();
          if ( contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                  .body("Unsupported media type");
          }
          // luu file da cap nhat thumbnail trong DTO
          String fileName = storeFile(file);
          log.info("File name: " + fileName);
          // save image vào productImg
          ProductImage productImage = productService.createProductImage(newProduct.getId(),
                ProductImageDTO.builder()
                      .imageUrl(fileName)
                      .build()
          );
          log.info("Product image: " + productImage);
        }
      }
      return ResponseEntity.ok("created product successfully!!!");

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
    try{
      productService.deleteProduct(id);
      return ResponseEntity.ok("delete susscessfully with productID = " + id);
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  //  @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  private String storeFile(MultipartFile file) throws IOException {

    if (!isImageFile(file) || file.getOriginalFilename() == null) {
//      throw new IOException("Invalid image file");
      throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid image type");
    }
//    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    // them UUID vao ten file de dam bao la duy nhat
    String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
      java.nio.file.Path uploadDir = Paths.get("images");
      // kiem tra va tao thu muc neu khong ton tai
      if (!Files.exists(uploadDir)) {
        Files.createDirectories(uploadDir);
      }

      Path path = Paths.get(uploadDir.toString(), uniqueFileName);
      Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
      return path.toString();
    }

    // check file co phai file image khong
    private boolean isImageFile (MultipartFile file){
//    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
      String contentType = file.getContentType();
      return contentType != null && contentType.startsWith("image/");
    }


    // fake data products
    @PostMapping("/generateFakeProducts")
    public ResponseEntity<?> generateFakeProducts() {
      int totalProducts = 1_000_000;
      int batchSize = 10_000;  // Chia nhỏ thành batch để insert tối ưu hơn
      int numThreads = Runtime.getRuntime().availableProcessors();  // Số luồng dựa trên CPU

      ExecutorService executor = Executors.newFixedThreadPool(numThreads);
      Faker faker = new Faker();
      Random random = new Random();

      for (int i = 0; i < totalProducts; i += batchSize) {
        int start = i;
        executor.submit(() -> {
          List<ProductDTO> batchProducts = new ArrayList<>();
          for (int j = start; j < start + batchSize && j < totalProducts; j++) {
            String productName = faker.commerce().productName();
            long categoryId = faker.number().numberBetween(1, 4);

            // Kiểm tra nếu tên đã tồn tại, bỏ qua
            if (productService.existByName(productName)) {
              continue;
            }

            // Sinh mã sản phẩm
            String productCode = productCodeGeneratorService.generateProductCode(productName, categoryId);

            ProductDTO productDTO = ProductDTO.builder()
                  .code(productCode)
                  .name(productName)
                  .price((float) faker.number().numberBetween(10, 90_000_000))
                  .thumbnail(faker.internet().image())
                  .description(faker.lorem().sentence())
                  .categoryId(categoryId)
                  .build();

            batchProducts.add(productDTO);
          }

          // Lưu danh sách sản phẩm trong batch (có thể tối ưu với batch insert)
          try {
            productService.createMultipleProducts(batchProducts);
          } catch (Exception e) {
            log.error("Error inserting batch products: ", e);
          }
        });
      }

      executor.shutdown();
      return ResponseEntity.ok("Generating products in background...");
    }


}
