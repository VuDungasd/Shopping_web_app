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

  @JsonProperty("user_id")
  @Min(value = 1, message = "UserID must be > 0")
  private Long userId;

  @JsonProperty("fullname")
  private String fullName;

  private String email;

  @JsonProperty("phone_number")
  @NotBlank(message = "Phone number required!")
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

}
