package io.github.gomestkdev.backend.Services.interfaces;

import io.github.gomestkdev.backend.models.CategoryModel;

import java.util.List;

public interface CategoryServiceImpl {
    List<CategoryModel> getAllCategory();
    void createCategory(CategoryModel category);
    CategoryModel updateCategory(Long id, CategoryModel category);
    String deleteCategory(Long id);
}
