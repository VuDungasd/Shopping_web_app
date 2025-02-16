package com.project.shopping_app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

  @JsonProperty("fullname")
  private String fullName;

  @JsonProperty("phone_number")
  @NotBlank(message = "Not empty in phone number!")
  private String phoneNumber;

  private String address;

  @NotBlank(message = "Not empty in password!")
  private String password;

  @JsonProperty("retype_password")
  private String retypePassword;

  @JsonProperty("date_of_birth")
  private Date dateOfBirth;

  @JsonProperty("facebook_account_id")
  private Integer facebookAccountId;

  @JsonProperty("google_account_id")
  private Integer googleAccountId;

  @JsonProperty("role_id")
  private Long roleID;

}
