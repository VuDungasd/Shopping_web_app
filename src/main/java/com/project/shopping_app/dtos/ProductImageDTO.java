package com.project.shopping_app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopping_app.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
  @JsonProperty("product_id")
  private Long productId;

  @Size(message = "image url")
  @JsonProperty("image_url")
  private String imageUrl;
}
