package com.project.shopping_app.service;

import com.project.shopping_app.dtos.ProductDTO;
import com.project.shopping_app.dtos.ProductImageDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Product;
import com.project.shopping_app.model.ProductImage;
import com.project.shopping_app.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

  public Product createProduct(ProductDTO productDTO) throws Exception;

  public Product getProductById(Long id);

  Page<ProductResponse> getAllProducts(PageRequest pageRequest);

  public Page<ProductResponse> getProductByFilter(String keyword, Long categoryId, PageRequest pageRequest);
  Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

  void deleteProduct(Long id);

  boolean existByName(String name);

  public ProductImage createProductImage(
        Long productID,
        ProductImageDTO productImageDTO) throws Exception;

  public void createMultipleProducts(List<ProductDTO> productDTOList) throws DataNotFoundException;

}
