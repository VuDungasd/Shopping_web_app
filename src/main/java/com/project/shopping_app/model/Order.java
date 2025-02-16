package com.project.shopping_app.model;


import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "fullname")
  private String fullname;

  @Column(name = "email")
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "address")
  private String address;

  @Column(name = "note")
  private String note;

  @Column(name = "order_date")
  private Timestamp orderDate;

  @Column(name = "status")
  private String status;

  @Column(name = "total_money")
  private Double totalMoney;

  @Column(name = "shipping_method")
  private String shippingMethod;

  @Column(name = "shipping_address")
  private String shippingAddress;

  @Column(name = "shipping_date")
  private Timestamp shippingDate;

  @Column(name = "tracking_number")
  private String trackingNumber;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "active")
  private Boolean active;
}
