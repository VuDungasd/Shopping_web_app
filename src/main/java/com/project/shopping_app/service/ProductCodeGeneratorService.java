package com.project.shopping_app.service;

import com.project.shopping_app.model.Category;
import com.project.shopping_app.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Service
public class ProductCodeGeneratorService {
  public final CategoryRepository categoryRepository;
  private final CategoryService categoryService;
  public String generateProductCode(String productName, long categoryId) {
    String categoryName = categoryService.getCategoryById(categoryId).getName();
    if(categoryName == null || productName == null) {
      throw new IllegalArgumentException("Invalid product name or product category");
    }
    // convert product name and category name  from vietnamese => english (hoa khong dau)
    // Chuyển đổi thành chữ hoa không dấu
    String normalizedProductName = removeDiacritics(productName).toUpperCase();
    String normalizedCategoryName = removeDiacritics(categoryName).toUpperCase();

    // Lấy chữ cái đầu của mỗi từ trong tên sản phẩm
    StringBuilder productInitials = new StringBuilder();
    for (String word : normalizedProductName.split(" ")) {
      if (!word.isEmpty()) {
        productInitials.append(word.charAt(0));
      }
    }

    // Lấy chữ cái đầu của danh mục sản phẩm
    String categoryInitial = normalizedCategoryName.isEmpty() ? "" : String.valueOf(normalizedCategoryName.charAt(0));

    // Tạo số ngẫu nhiên 4 chữ số
    int randomNum = new Random().nextInt(9000) + 1000; // Random từ 1000 đến 9999

    // Tạo mã sản phẩm
    return productInitials + categoryInitial + "-" + randomNum;
  }

  // Hàm loại bỏ dấu tiếng Việt
  private static String removeDiacritics(String str) {
    String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(normalized).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
  }
}