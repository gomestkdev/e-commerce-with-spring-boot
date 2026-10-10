package io.github.gomestkdev.backend.repositories;

import io.github.gomestkdev.backend.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, Long> { }
