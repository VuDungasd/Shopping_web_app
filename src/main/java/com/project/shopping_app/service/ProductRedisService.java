package com.project.shopping_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.shopping_app.response.ProductResponse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductRedisService {

  // clear cached data in redis
  void clear(); // clear cache

  List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException;

  void saveAllProducts(List<ProductResponse> productResponses,
                       String keyword,
                       Long categoryId,
                       PageRequest pageRequest) throws JsonProcessingException;
}
