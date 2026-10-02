package io.github.gomestkdev.backend.controllers;

import io.github.gomestkdev.backend.Services.CategoryService;
import io.github.gomestkdev.backend.config.AppConstants;
import io.github.gomestkdev.backend.dtos.CategoryDTO;
import io.github.gomestkdev.backend.payload.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer number,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer size,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) throws Throwable {
        try {
            CategoryResponse categoriesResponse = service.getAllCategory(number, size, sortBy, sortOrder);
            return new ResponseEntity<>(categoriesResponse, OK);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }

    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO category) throws Throwable {
        try {
            CategoryDTO createdCategory = service.createCategory(category);
            return new ResponseEntity<>(createdCategory, CREATED);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id, @Valid @RequestBody CategoryDTO category
    ) throws Throwable {
        try {
            CategoryDTO savedCategory = service.updateCategory(id, category);
            return ResponseEntity.status(OK).body(savedCategory);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) throws Throwable {
        try {
            CategoryDTO deletedCategory = service.deleteCategory(id);
            return ResponseEntity.status(OK).body(deletedCategory);
        } catch (ResponseStatusException e) {
            throw new Throwable(e.getReason());
        }
    }
}
