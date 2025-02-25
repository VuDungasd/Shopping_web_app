package com.project.shopping_app.model;

import com.project.shopping_app.service.ProductRedisService;
import com.project.shopping_app.service.ProductService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ProductListener {
  private final ProductRedisService productRedisService;

  @PrePersist
  public void prePersist(Product product) {
    log.info("Pre persist product {}", product);
  }

  @PostPersist // save = persis
  public void postPersist(Product product) {
    productRedisService.clear();
  }

  @PreUpdate
  public void preUpdate(Product product) {
    log.info("Pre update product {}", product);
  }

  @PostUpdate
  public void postUpdate(Product product) {
    log.info("Post update product {}", product);
    productRedisService.clear();
  }

  @PreRemove
  public void preRemove(Product product) {
    log.info("preRemove product {}", product);
  }

  @PostRemove
  public void postRemove(Product product) {
    log.info("postRemove");
    productRedisService.clear();
  }

}
