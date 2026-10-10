package io.github.gomestkdev.backend.controllers;

import io.github.gomestkdev.backend.services.CategoryServiceImpl;
import io.github.gomestkdev.backend.config.AppConstants;
import io.github.gomestkdev.backend.dtos.CategoryDTO;
import io.github.gomestkdev.backend.payload.CategoryResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryServiceImpl service;

    public CategoryController(CategoryServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CategoryResponse> getAllCategories(
            @Parameter(description = "Page number to retrieve (starts from 0)", example = "0")
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer number,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer size,

            @Parameter(description = "Field to sort the records by", example = "name")
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,

            @Parameter(description = "Sorting direction: asc (ascending) or desc (descending)", example = "asc")
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        CategoryResponse categoriesResponse = service.getAllCategories(number, size, sortBy, sortOrder);
        return new ResponseEntity<>(categoriesResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO createdCategory = service.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryDTO category
    ) {
        CategoryDTO savedCategory = service.updateCategory(id, category);
        return ResponseEntity.status(HttpStatus.OK).body(savedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        CategoryDTO deletedCategory = service.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedCategory);
    }
}