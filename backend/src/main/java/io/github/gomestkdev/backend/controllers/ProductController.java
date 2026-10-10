package io.github.gomestkdev.backend.controllers;

import io.github.gomestkdev.backend.services.ProductServiceImpl;
import io.github.gomestkdev.backend.config.AppConstants;
import io.github.gomestkdev.backend.dtos.ProductDTO;
import io.github.gomestkdev.backend.payload.ProductResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductServiceImpl service;

    public ProductController(ProductServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts(
            @Parameter(description = "Page number to retrieve (starts from 0)", example = "0")
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,

            @Parameter(description = "Field to sort the records by", example = "name")
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,

            @Parameter(description = "Sorting direction: asc (ascending) or desc (descending)", example = "asc")
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = service.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductResponse> getProductByCategory(
            @PathVariable Long categoryId,

            @Parameter(description = "Page number to retrieve (starts from 0)", example = "0")
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,

            @Parameter(description = "Field to sort the records by", example = "name")
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,

            @Parameter(description = "Sorting direction: asc (ascending) or desc (descending)", example = "asc")
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = service.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable String keyword,

            @Parameter(description = "Page number to retrieve (starts from 0)", example = "0")
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,

            @Parameter(description = "Number of records per page", example = "10")
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,

            @Parameter(description = "Field to sort the records by", example = "name")
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,

            @Parameter(description = "Sorting direction: asc (ascending) or desc (descending)", example = "asc")
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = service.searchByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody ProductDTO product
    ) {
        ProductDTO newProduct = service.addProduct(categoryId, product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        ProductDTO updatedProductWithImage = service.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProductWithImage, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product
    ) {
        ProductDTO productDTO = service.updateProduct(productId, product);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO product = service.deleteProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}