package com.project.shopping_app.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "social_accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Social_account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "provider")
  private String provider;

  @Column(name = "provider_id")
  private Long providerId;

  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

}
