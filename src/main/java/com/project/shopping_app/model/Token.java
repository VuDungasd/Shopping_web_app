package com.project.shopping_app.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Token")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token", nullable = false, length = 255)
  private String token;

  @Column(name = "token_type", length = 50)
  private String tokenType;

  @Column(name = "expiration_date")
  private LocalDateTime  expirationDate;

  @Column(name = "revoked")
  private Boolean revoked; // huy bo token

  @Column(name = "expired")
  private Boolean expired; // het han token

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
