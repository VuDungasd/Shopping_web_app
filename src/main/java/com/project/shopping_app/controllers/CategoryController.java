package com.project.shopping_app.controllers;

import com.project.shopping_app.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
public class CategoryController {

  // get all category
  @GetMapping("")
  public ResponseEntity<String> getAllCategory( // ex: localhost:8080/api/v1/categories?page=18&limit=10
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
  ) {
    return ResponseEntity.ok(String.format("Page: %d, , Limit: %d", page, limit));
  }

  @PostMapping("")
  public ResponseEntity<?> createCategory(
        @Valid @RequestBody CategoryDTO categoryDTO,
        BindingResult result
  ){
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .toList();
      return ResponseEntity.badRequest().body(errorMessages);
    }
    return ResponseEntity.ok("Hello world test categorie " + categoryDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateCategory(@PathVariable("id") Long id){
    return ResponseEntity.ok("Update categories with id: " + id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id){
    return ResponseEntity.ok("Delete categories with id: " + id);
  }
}
