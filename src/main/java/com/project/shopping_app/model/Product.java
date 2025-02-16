package com.project.shopping_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, length = 50)
  private String code;

  private String name;

  @Column(name = "price", nullable = false, length = 300)
  @Min(value = 0)
  private Double price;

  @Column(name = "thumbnail", length = 300)
  private String thumbnail;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_product_category"))
  private Category category;
}
