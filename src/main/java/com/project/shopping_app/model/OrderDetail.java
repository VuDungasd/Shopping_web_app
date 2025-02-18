package com.project.shopping_app.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "order_details")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "price")
  private double price;

  @Column(name = "number_of_products")
  private int numberOfProducts;

  @Column(name = "total_money")
  private Double totalMoney;

  @Column(name = "color")
  private String color;

}
