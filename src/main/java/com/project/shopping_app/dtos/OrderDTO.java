package com.project.shopping_app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

  @Min(value = 1, message = "UserID must be > 0")
  @JsonProperty(namespace = "user_id")
  private Long userId;

  @JsonProperty(namespace = "fullname")
  private String fullName;

  private String email;

  @JsonProperty(namespace = "phone_number")
  @NotBlank(message = "Phone number required!")
  private String phoneNumber;

  private String address;

  private String note;

  @JsonProperty(namespace = "total_money")
  private Float totalMoney;

  @JsonProperty(namespace = "shipping_method")
  private String shippingMethod;

  @JsonProperty(namespace = "shipping_address")
  private String shippingAddress;

  @JsonProperty(namespace = "payment_method")
  private String paymentMethod;

}
