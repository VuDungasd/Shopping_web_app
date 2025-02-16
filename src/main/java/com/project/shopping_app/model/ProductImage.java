package com.project.shopping_app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "product_images")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImage {
  public static final int MAXIMUM_IMAGES_PER_PRODUCT = 5;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "image_url", length = 300)
  private String imageUrl;

}
