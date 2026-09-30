package io.github.gomestkdev.backend.controllers;

import io.github.gomestkdev.backend.Services.CategoryService;
import io.github.gomestkdev.backend.models.CategoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryModel>> getAllCategories() throws Throwable {
        try {
            List<CategoryModel> categories = categoryService.getAllCategory();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }

    @PostMapping()
    public ResponseEntity<String> createCategory(@RequestBody CategoryModel category) throws Throwable {
        try {
            categoryService.createCategory(category);
            return new ResponseEntity<>("Category added with successfully", HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id, @RequestBody CategoryModel category
    ) throws Throwable {
        try {
            CategoryModel status = categoryService.updateCategory(id, category);
            return ResponseEntity.status(HttpStatus.OK).body("Category with id: " + id + " updated with successfully.");
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) throws Throwable {
        try {
            String status = categoryService.deleteCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }
}
