package io.github.gomestkdev.backend.repositories;

import io.github.gomestkdev.backend.models.CategoryModel;
import io.github.gomestkdev.backend.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByCategoryOrderByPriceAsc(CategoryModel category);
    List<ProductModel> findByProductNameLikeIgnoreCase(String keyword);
}
