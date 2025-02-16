package com.project.shopping_app.controllers;

import com.project.shopping_app.dtos.CategoryDTO;
import com.project.shopping_app.model.Category;
import com.project.shopping_app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;
  // get all category
  @GetMapping("")
  public ResponseEntity<?> getAllCategory( // ex: localhost:8080/api/v1/categories?page=18&limit=10
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
  ) {
    List<CategoryDTO> categoryDTOs = categoryService.getAllCategories();
    return ResponseEntity.ok(categoryDTOs);
  }

  @PostMapping("")
  public ResponseEntity<?> createCategory(
        @Valid @RequestBody CategoryDTO categoryDTO,
        BindingResult result ){
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .toList();
      return ResponseEntity.badRequest().body(errorMessages);
    }
    categoryService.createCategory(categoryDTO);
    return ResponseEntity.ok("Hello world test categories " + categoryDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO ) {
    categoryService.updateCategory(id, categoryDTO);
    return ResponseEntity.ok("Update categories with categoryID: " + id);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id){
    categoryService.deleteCategory(id);
    return ResponseEntity.ok("Delete categories with id: " + id);
  }
}
