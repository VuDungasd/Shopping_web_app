package com.project.shopping_app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fullname", length = 300)
  private String fullname;

  @Column(name = "phone_number", length = 10, nullable = false)
  private String phoneNumber;

  @Column(name = "address")
  private String address;

  @Column(name = "password", length = 100, nullable = false)
  private String password;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "date_of_birt")
  private Date dateOfBirth;

  @Column(name = "facebook_account_id")
  private Integer facebookAccountId;

  @Column(name = "google_account_id")
  private Integer googleAccountId;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

}
