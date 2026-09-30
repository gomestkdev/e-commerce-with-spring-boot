package io.github.gomestkdev.backend.Services;

import io.github.gomestkdev.backend.Services.interfaces.CategoryServiceImpl;
import io.github.gomestkdev.backend.models.CategoryModel;
import io.github.gomestkdev.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CategoryService implements CategoryServiceImpl {
    @Autowired
    private CategoryRepository repository;

    @Override
    public List<CategoryModel> getAllCategory() {
        return repository.findAll();
    }

    @Override
    public void createCategory(CategoryModel category) {
        repository.save(category);
    }

    @Override
    public CategoryModel updateCategory(Long id, CategoryModel category) {
        Optional<CategoryModel> isCategory = repository.findById(id);

        CategoryModel categoryFound = isCategory.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Resource not found!"));
        category.setId(id);

        categoryFound = repository.save(category);

        return categoryFound;
    }

    @Override
    public String deleteCategory(Long id) {
        Optional<CategoryModel> isCategory = repository.findById(id);

        CategoryModel categoryFound = isCategory.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Resource not found!"));

        repository.delete(categoryFound);

        return "Category with id: " + id + " deleted successfully!";
    }
}
