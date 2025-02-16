package com.project.shopping_app.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

  @JsonProperty("order_id")
  @Min(value = 1, message = "Order_id >= 1")
  private Long orderId;

  @JsonProperty("product_id")
  @Min(value = 1, message = "Product_ID >= 1")
  private Long productId;

  @JsonProperty("price")
  @Min(value = 1, message = "price >= 0")
  private String price;

  @JsonProperty("number_of_products")
  @Min(value = 1, message = "quantity >= 1")
  private Integer numberOfProducts;


  @JsonProperty("total_money")
  @Min(value = 1, message = "total money >= 0")
  private String totalMoney;

  @JsonProperty("color")
  private String color;
}
