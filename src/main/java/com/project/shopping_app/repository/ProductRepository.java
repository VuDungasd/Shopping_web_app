package com.project.shopping_app.repository;

import com.project.shopping_app.model.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsByName(String name);

  Optional<Product> findById(Long id);

  @Override
  Page<Product> findAll(Pageable pageable);

  @Query("SELECT p FROM Product p WHERE " +
        "(:categoryId IS NULL OR p.category.id = :categoryId) " +
        "AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
  Page<Product> searchProducts(@Param("categoryId") Long categoryId,
                               @Param("keyword") String keyword,
                               Pageable pageable);

}
