package io.github.gomestkdev.backend.Services.interfaces;

import io.github.gomestkdev.backend.dtos.CategoryDTO;
import io.github.gomestkdev.backend.payload.CategoryResponse;

public interface CategoryServiceImpl {
    CategoryResponse getAllCategory(
            Integer pageNumber, Integer pageSize, String sortBy, String sortOrder
    );
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(Long id, CategoryDTO category);
    CategoryDTO deleteCategory(Long id);
}
