package io.github.gomestkdev.backend.controllers;

import io.github.gomestkdev.backend.services.ProductServiceImpl;
import io.github.gomestkdev.backend.config.AppConstants;
import io.github.gomestkdev.backend.dtos.ProductDTO;
import io.github.gomestkdev.backend.payload.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.FOUND;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductServiceImpl service;

    public ProductController(ProductServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false)  String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = service.getAllProducts(
                pageNumber,
                pageSize,
                sortBy,
                sortOrder
        );

        return new ResponseEntity<>(productResponse, OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ProductResponse> getProductByCategory(
            @PathVariable Long categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false)  String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = service.searchByCategory(
                categoryId,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder
        );
        return new ResponseEntity<>(productResponse, OK);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false)  Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false)  String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ) {
        ProductResponse productResponse = service.searchByKeyword(
                keyword,
                pageNumber,
                pageSize,
                sortBy,
                sortOrder
        );
        return new ResponseEntity<>(productResponse, FOUND);
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<?> addProduct(
            @PathVariable Long categoryId,
            @Valid @RequestBody ProductDTO product
    ) {
        ProductDTO newProduct = service.addProduct(categoryId, product);

        return new ResponseEntity<>(newProduct, CREATED);
    }

    @PutMapping("/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        ProductDTO updatedProductWithImage = service.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProductWithImage, OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductDTO product
    ) {
        ProductDTO productDTO = service.updateProduct(productId, product);
        return new ResponseEntity<>(productDTO, OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId) {
        ProductDTO product = service.delete(productId);

        return new ResponseEntity<>(product, OK);
    }

}
