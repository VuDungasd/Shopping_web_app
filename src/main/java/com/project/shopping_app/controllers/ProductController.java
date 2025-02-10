package com.project.shopping_app.controllers;

import com.project.shopping_app.dtos.ProductDTO;
import jakarta.validation.Valid;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;

@RestController
@RequestMapping("api/v1/products")
@Validated
public class ProductController {

  @GetMapping("")
  public ResponseEntity<?> getAllProduct(
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
  ) {
    return ResponseEntity.ok("page: " + page + ", limit: " + limit);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
    return ResponseEntity.ok("get product with id: " + id);
  }

  @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createProduct(
        @Valid @ModelAttribute ProductDTO productDTO,
        BindingResult result ) {
    try {
      if (result.hasErrors()) {
        List<String> errorMessage = result.getFieldErrors()
              .stream()
              //            .map(fieldError -> fieldError.getDefaultMessage())
              .map(FieldError::getDefaultMessage)
              .toList();
        return ResponseEntity.badRequest().body(errorMessage);
      }
      List<MultipartFile> files = productDTO.getFiles();
      // check size file >= 10 mb return false, else oke
      files = files == null ? new ArrayList<MultipartFile>() : files;
      for(MultipartFile file : files){
        if(file != null){
          if(file.getSize() == 0){
            continue;
          }
          if (file.getSize() > 1024 * 1024 * 10) { // size > 10mb
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File to large, Maximum size 10mb");
          }
          String contentType = file.getContentType();
          if(contentType == null || !contentType.startsWith("image/")){
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                  .body("Unsupported media type");
          }
          // luu file da cap nhat thumbnail trong DTO
          String fileName = storeFile(file);
          // save vao db
        }
      }
      return ResponseEntity.ok("created product successfully!!!");
      // luu vao doi tuong product trong db
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  private String storeFile(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    // them UUID vao ten file de dam bao la duy nhat
    String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
    java.nio.file.Path uploadDir = Paths.get("images");
    // kiem tra va tao thu muc neu khong ton tai
    if(!Files.exists(uploadDir)){
      Files.createDirectories(uploadDir);
    }

    Path path = Paths.get(uploadDir.toString(), uniqueFileName);
    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    return path.toString();
  }
}
