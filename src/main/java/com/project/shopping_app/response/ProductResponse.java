package com.project.shopping_app.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopping_app.model.Product;
import com.project.shopping_app.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
  private Long id;
  private String code;
  private String name;
  private Double price;
  private String thumbnail;
  private String description;
  // Thêm trường totalPages
  private int totalPages;

  @JsonProperty("product_images")
  private List<ProductImage> productImages = new ArrayList<>();

  @JsonProperty("category_id")
  private Long categoryId;
  public static ProductResponse fromProduct(Product product) {
    ProductResponse productResponse = ProductResponse.builder()
            .id(product.getId())
            .code(product.getCode())
            .name(product.getName())
            .price(product.getPrice())
            .thumbnail(product.getThumbnail())
            .description(product.getDescription())
            .categoryId(product.getCategory().getId())
            .build();
      productResponse.setCreatedAt(product.getCreatedAt());
      productResponse.setUpdatedAt(product.getUpdatedAt());
      return productResponse;
  }
}

