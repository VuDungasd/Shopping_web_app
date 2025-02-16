package com.project.shopping_app.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
  @NotBlank(message = "Not empty in product name!")
  @Size(min = 4, max = 200, message = "Title must be between 3 and 200 characters")
  private String name;

  private String description;

  @Min(value = 0, message = "Product must be greater than or equal to 0")
  private double price;

  private String thumbnail;

  @JsonProperty("category_id")
  private Long categoryId;

  private List<MultipartFile> files;
}
