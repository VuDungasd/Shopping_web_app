package com.project.shopping_app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopping_app.response.ProductResponse;
import com.project.shopping_app.service.ProductRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductRedisServiceImpl implements ProductRedisService {

  private final RedisTemplate<String, String> redisTemplate;

  private final ObjectMapper objectMapper;

  private String getKeyFrom(String keyword, Long categoryId, PageRequest pageRequest) {
    int pageNumber = pageRequest.getPageNumber();
    int pageSize = pageRequest.getPageSize();
    Sort sort = pageRequest.getSort();

    Sort.Order order = sort.getOrderFor("id"); // Kiểm tra order có null hay không
    String sortDirection = (order != null && order.getDirection() == Sort.Direction.ASC) ? "asc" : "desc";

    // Đảm bảo keyword không null
    String sanitizedKeyword = (keyword != null) ? keyword.toLowerCase().trim() : "";

    return String.format("all_products:%s:%d:%d:%d:%s", sanitizedKeyword, categoryId, pageNumber, pageSize, sortDirection);

  }

  @Override
  public void clear() {
    redisTemplate.getConnectionFactory().getConnection().flushAll();
//    Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();
  }

  @Override
  public List<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
    String key = this.getKeyFrom(keyword, categoryId, pageRequest);
    String value = redisTemplate.opsForValue().get(key);
    List<ProductResponse> productResponses =
          value != null ? objectMapper.readValue(value, new TypeReference<List<ProductResponse>>() {}) : null;
    return productResponses;
  }

  @Override
  public void saveAllProducts(List<ProductResponse> productResponses, String keyword, Long categoryId, PageRequest pageRequest) throws JsonProcessingException {
    String key = this.getKeyFrom(keyword, categoryId, pageRequest);
    String value = objectMapper.writeValueAsString(productResponses);
    redisTemplate.opsForValue().set(key, value);
  }
}
