package io.github.gomestkdev.backend.Services;

import io.github.gomestkdev.backend.Services.interfaces.CategoryServiceImpl;
import io.github.gomestkdev.backend.exeptions.handler.ApiException;
import io.github.gomestkdev.backend.exeptions.handler.ResourceNotFoundException;
import io.github.gomestkdev.backend.models.CategoryModel;
import io.github.gomestkdev.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements CategoryServiceImpl {
    @Autowired
    private CategoryRepository repository;

    @Override
    public List<CategoryModel> getAllCategory() {
        List<CategoryModel> categories = repository.findAll();

        if (categories.isEmpty()) {
            throw new ApiException("No category created till now.");
        }
        return categories;
    }

    @Override
    public void createCategory(CategoryModel category) {
        CategoryModel savedCategory = repository.findByName(category.getName());
        if (savedCategory != null) {
            throw new ApiException("Category with name: " + category.getName() + " already exists!.");
        }

        repository.save(category);
    }

    @Override
    public CategoryModel updateCategory(Long id, CategoryModel category) {
        Optional<CategoryModel> isCategory = repository.findById(id);

        isCategory.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        CategoryModel categoryFound;
        category.setId(id);

        categoryFound = repository.save(category);

        return categoryFound;
    }

    @Override
    public String deleteCategory(Long id) {
        Optional<CategoryModel> isCategory = repository.findById(id);

        CategoryModel categoryFound = isCategory.orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        repository.delete(categoryFound);

        return "Category with id: " + id + " deleted successfully!";
    }
}
