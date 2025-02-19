package com.project.shopping_app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
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

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "facebook_account_id")
  private Integer facebookAccountId;

  @Column(name = "google_account_id")
  private Integer googleAccountId;

  @ManyToOne
  @JoinColumn(name = "role_id")
  private Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorityLists = new ArrayList<>();
    authorityLists.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    return authorityLists;
  }

  @Override
  public String getUsername() {
    return phoneNumber;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
