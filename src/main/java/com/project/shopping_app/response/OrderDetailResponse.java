package com.project.shopping_app.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopping_app.model.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
  private Long id;

  @JsonProperty("order_id")
  private Long orderId;

  @JsonProperty("product_id")
  private Long productId;

  @JsonProperty("price")
  private Double price;

  @JsonProperty("number_of_products")
  private int numberOfProducts;

  @JsonProperty("total_money")
  private Double totalMoney;

  @JsonProperty("color")
  private String color;

  public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
    return OrderDetailResponse
          .builder()
          .id(orderDetail.getId())
          .orderId(orderDetail.getOrder().getId())
          .productId(orderDetail.getProduct().getId())
          .price(orderDetail.getPrice())
          .numberOfProducts(orderDetail.getNumberOfProducts())
          .totalMoney(orderDetail.getTotalMoney())
          .color(orderDetail.getColor())
          .build();
  }

}
