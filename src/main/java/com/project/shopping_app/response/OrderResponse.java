package com.project.shopping_app.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopping_app.model.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse extends BaseResponse {
  @JsonProperty("user_id")
  private Long userId;
  @JsonProperty("fullname")
  private String fullName;

  private String email;

  @JsonProperty("phone_number")
  private String phoneNumber;

  private String address;

  private String note;

  @JsonProperty("total_money")
  private Double totalMoney;

  @JsonProperty("shipping_method")
  private String shippingMethod;

  @JsonProperty("shipping_address")
  private String shippingAddress;

  @JsonProperty("payment_method")
  private String paymentMethod;

  public static OrderResponse fromOrder(Order order) {
    OrderResponse orderResponse = OrderResponse.builder()
          .userId(order.getId())
          .fullName(order.getFullname())
          .email(order.getEmail())
          .phoneNumber(order.getPhoneNumber())
          .address(order.getAddress())
          .note(order.getNote())
          .totalMoney(order.getTotalMoney())
          .shippingMethod(order.getShippingMethod())
          .shippingAddress(order.getShippingAddress())
          .paymentMethod(order.getPaymentMethod())
          .build();
    orderResponse.setCreatedAt(order.getCreatedAt());
    orderResponse.setUpdatedAt(order.getUpdatedAt());
    return orderResponse;

  }

}
