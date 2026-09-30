package io.github.gomestkdev.backend.Services;

import io.github.gomestkdev.backend.Services.interfaces.CategoryServiceImpl;
import io.github.gomestkdev.backend.models.CategoryModel;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CategoryService implements CategoryServiceImpl {
    private List<CategoryModel> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<CategoryModel> getAllCategory() {
        return categories;
    }

    @Override
    public void createCategory(CategoryModel category) {
        category.setId(nextId++);
        categories.add(category);
    }

    @Override
    public CategoryModel updateCategory(Long id, CategoryModel category) {
        Optional<CategoryModel> categoryFound = categories.stream()
                .filter(c -> c.getId().equals(id)).
                findFirst();

        if (categoryFound.isPresent()) {
            CategoryModel existingCategory = categoryFound.get();
            existingCategory.setName(category.getName());
            return existingCategory;
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Category not found!");
        }
    }

    @Override
    public String deleteCategory(Long id) {
        CategoryModel category = categories.stream()
                .filter(c -> c.getId().equals(id)).
                findFirst()
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        categories.remove(category);

        return "Category with id: " + id + " deleted successfully!";
    }
}
