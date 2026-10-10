package io.github.gomestkdev.backend.controllers;

import io.github.gomestkdev.backend.Services.CategoryServiceImpl;
import io.github.gomestkdev.backend.Services.interfaces.CategoryService;
import io.github.gomestkdev.backend.config.AppConstants;
import io.github.gomestkdev.backend.dtos.CategoryDTO;
import io.github.gomestkdev.backend.payload.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer number,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        CategoryResponse categoriesResponse = service.getAllCategory(number, size, sortBy, sortOrder);
        return new ResponseEntity<>(categoriesResponse, OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO createdCategory = service.createCategory(category);
        return new ResponseEntity<>(createdCategory, CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryDTO category
    ) {
        CategoryDTO savedCategory = service.updateCategory(id, category);
        return ResponseEntity.status(OK).body(savedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        CategoryDTO deletedCategory = service.deleteCategory(id);
        return ResponseEntity.status(OK).body(deletedCategory);
    }
}