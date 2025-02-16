package com.project.shopping_app.repository;

import com.project.shopping_app.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {


  public List<ProductImage> findAllByProductId(Long productId);


}
