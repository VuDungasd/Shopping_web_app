package com.project.shopping_app.service;

import com.project.shopping_app.dtos.ProductDTO;
import com.project.shopping_app.dtos.ProductImageDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Product;
import com.project.shopping_app.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {

  public Product createProduct(ProductDTO productDTO) throws Exception;

  public Product getProductById(Long id);

  Page<Product> getAllProducts(PageRequest pageRequest);

  Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

  void deleteProduct(Long id);

  boolean existByName(String name);

  public ProductImage createProductImage(
        Long productID,
        ProductImageDTO productImageDTO) throws Exception;
}
