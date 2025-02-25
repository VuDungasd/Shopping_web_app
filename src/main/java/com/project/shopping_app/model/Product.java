package com.project.shopping_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(ProductListener.class)
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

  @Column(name = "active")
  private Boolean active = true;

  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_product_category"))
  private Category category;
}
