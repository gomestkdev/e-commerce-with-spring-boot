package io.github.gomestkdev.backend.repositories;

import io.github.gomestkdev.backend.models.CategoryModel;
import io.github.gomestkdev.backend.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    Page<ProductModel> findByCategory(CategoryModel category, Pageable pageable);
    Page<ProductModel> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}