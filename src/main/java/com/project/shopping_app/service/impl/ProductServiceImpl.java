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
import com.project.shopping_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final ProductImageRepository productImageRepository;

  @Override
  public Product createProduct(ProductDTO productDTO) throws Exception {

    Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
          .orElseThrow(() -> new DataNotFoundException("Category not found with categoryID" + productDTO.getCategoryId()));

    Product product = Product
          .builder()
          .name(productDTO.getName())
          .price(productDTO.getPrice())
          .description(productDTO.getDescription())
          .category(existingCategory)
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
  public Page<Product> getAllProducts(PageRequest pageRequest) {
    return productRepository.findAll(pageRequest);
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
      productRepository.deleteById(id);
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
    if (productImageRepository.findAllByProductId(productID).size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
      throw new InvalidParamException("Qua so luong anh");
    }

    ProductImage productImage = ProductImage.builder()
          .product(product)
          .imageUrl(productImageDTO.getImageUrl())
          .build();
    return productImageRepository.save(productImage);
  }


}
