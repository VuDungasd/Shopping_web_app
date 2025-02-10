package com.project.shopping_app.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.logging.log4j.message.Message;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
  @NotEmpty(message = "Not empty in product name!")
  private String name;
  private String description;
}
