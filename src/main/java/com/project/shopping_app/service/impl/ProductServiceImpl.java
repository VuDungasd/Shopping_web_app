package com.project.shopping_app.service.impl;

import com.project.shopping_app.dtos.ProductDTO;
import com.project.shopping_app.dtos.ProductImageDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.exceptions.InvalidParamException;
import com.project.shopping_app.model.Category;
import com.project.shopping_app.model.Product;
import com.project.shopping_app.model.ProductImage;
import com.project.shopping_app.repository.CategoryRepository;
import com.project.shopping_app.repository.ProductImageRepository;
import com.project.shopping_app.repository.ProductRepository;
import com.project.shopping_app.response.ProductResponse;
import com.project.shopping_app.service.ProductCodeGeneratorService;
import com.project.shopping_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final ProductImageRepository productImageRepository;
  private final ProductCodeGeneratorService productCodeGeneratorService;

  @Override
  public Product createProduct(ProductDTO productDTO) throws Exception {

    Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
          .orElseThrow(() -> new DataNotFoundException("Category not found with categoryID" + productDTO.getCategoryId()));

    Product product = Product.builder()
          .code(productCodeGeneratorService.generateProductCode(productDTO.getName(), productDTO.getCategoryId()))
          .name(productDTO.getName())
          .price(productDTO.getPrice())
          .description(productDTO.getDescription())
          .category(existingCategory)
          .active(true)
          .build();

    return productRepository.save(product);
  }

  @Override
  public Product getProductById(Long id) {
    try {
      return productRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Not found ProductID"));
    } catch (DataNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {

    // get list product by page and limit
    // Kiểm tra nếu active không phải là 1, sẽ trả về null để không có sản phẩm này
    //      if (!product.getActive()) {
    //        return null;
    //      }
    return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
  }

  @Override
  public Page<ProductResponse> getProductByFilter(String keyword, Long categoryId, PageRequest pageRequest){
    // get product by filter from FE
    return productRepository.searchProducts(categoryId, keyword, pageRequest).map(product -> {
      //check active in product
      if (!product.getActive()) {
        return null;
      }
      return ProductResponse.fromProduct(product);
    });
  }

  @Override
  public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
    Product exitingProduct = getProductById(id);
    if (exitingProduct != null) {
      Category category = categoryRepository.findById(productDTO.getCategoryId())
            .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));
      exitingProduct.setName(productDTO.getName());
      exitingProduct.setPrice(productDTO.getPrice());
      exitingProduct.setDescription(productDTO.getDescription());
      exitingProduct.setThumbnail(productDTO.getThumbnail());
      exitingProduct.setCategory(category);
      return productRepository.save(exitingProduct);
    }
    return null;
  }

  @Override
  public void deleteProduct(Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isPresent()) {
      Product productToDelete = product.get();
      productToDelete.setActive(false);
      productRepository.save(productToDelete);
    }else{
      throw new RuntimeException("Product not found");
    }
  }

  @Override
  public boolean existByName(String name) {
    return productRepository.existsByName(name);
  }

  @Override
  public ProductImage createProductImage(
        Long productID,
        ProductImageDTO productImageDTO) throws Exception {
    Product product = productRepository.findById(productID)
          .orElseThrow(() -> new DataNotFoundException("Not found ProductID" + productImageDTO.getProductId()));

    // khong cho insert qua 5 anh trong 1 product
    if (productImageRepository.findAllByProductId(productID).size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
      throw new InvalidParamException("Qua so luong anh");
    }

    ProductImage productImage = ProductImage.builder()
          .product(product)
          .imageUrl(productImageDTO.getImageUrl())
          .build();
    return productImageRepository.save(productImage);
  }

  // insert fake data
  @Override
  public void createMultipleProducts(List<ProductDTO> productDTOList) throws DataNotFoundException {
    List<Product> products = new ArrayList<>();
    for (ProductDTO dto : productDTOList) {
      Category existingCategory = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new DataNotFoundException("Category not found with ID: " + dto.getCategoryId()));

      Product product = Product.builder()
            .code(dto.getCode())
            .name(dto.getName())
            .price(dto.getPrice())
            .description(dto.getDescription())
            .category(existingCategory)
            .active(true)
            .build();

      products.add(product);
    }

    // Batch insert để tối ưu hiệu suất
    productRepository.saveAll(products);
  }


}
