package com.project.shopping_app.service;


import com.project.shopping_app.dtos.CategoryDTO;
import com.project.shopping_app.model.Category;

import java.util.List;

public interface CategoryService {

  Category createCategory(CategoryDTO categoryDTO);

  Category getCategoryById(Long id);

  List<CategoryDTO> getAllCategories();

  Category updateCategory(Long categoryID, CategoryDTO categoryDTO);

  void deleteCategory(Long categoryID );
}
