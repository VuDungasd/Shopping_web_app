package com.project.shopping_app.service.impl;

import com.project.shopping_app.dtos.CategoryDTO;
import com.project.shopping_app.model.Category;
import com.project.shopping_app.repository.CategoryRepository;
import com.project.shopping_app.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Builder
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  public Category createCategory(CategoryDTO categoryDTO) {
    Category category = Category
          .builder()
          .name(categoryDTO.getName())
          .build();
    return categoryRepository.save(category);
  }

  @Override
  public Category getCategoryById(Long id) {
//    return categoryRepository.findById(id).orElse(null);
    return categoryRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Category not found"));
  }

  @Override
  public List<CategoryDTO> getAllCategories() {
    List<Category> categories = categoryRepository.findAll();
    return categories
          .stream()
          .map(category -> new CategoryDTO(category.getName()))
          .collect(Collectors.toList());
  }

  @Override
  public Category updateCategory(Long categoryID, @RequestBody CategoryDTO categoryDTO) {
    Category oldCategory = getCategoryById(categoryID);
    oldCategory.setName(categoryDTO.getName());
    categoryRepository.save(oldCategory);
    return oldCategory;
  }

  @Override
  public void deleteCategory(Long categoryID) {
    categoryRepository.deleteById(categoryID);
  }
}
