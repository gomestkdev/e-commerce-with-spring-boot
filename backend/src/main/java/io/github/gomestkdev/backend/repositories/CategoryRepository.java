package io.github.gomestkdev.backend.repositories;

import io.github.gomestkdev.backend.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
}
