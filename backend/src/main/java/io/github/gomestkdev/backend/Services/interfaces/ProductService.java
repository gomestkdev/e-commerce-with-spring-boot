package io.github.gomestkdev.backend.Services.interfaces;

import io.github.gomestkdev.backend.dtos.ProductDTO;
import io.github.gomestkdev.backend.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);
    ProductResponse getAllProducts(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    );
    ProductResponse searchByCategory(
            Long categoryId, 
            Integer pageNumber, 
            Integer pageSize, 
            String sortBy, 
            String sortOrder
    );
    ProductResponse searchByKeyword(
            String keyword,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortOrder
    );
    ProductDTO updateProduct(Long productId, ProductDTO product);
    ProductDTO delete(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

}
