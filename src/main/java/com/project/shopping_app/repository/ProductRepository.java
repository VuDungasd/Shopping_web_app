package com.project.shopping_app.repository;

import com.project.shopping_app.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsByName(String name);

  @Override
  Page<Product> findAll(Pageable pageable);

  Optional<Product> findById(Long id);
}
