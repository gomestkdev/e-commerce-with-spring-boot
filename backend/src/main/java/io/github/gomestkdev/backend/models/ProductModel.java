package io.github.gomestkdev.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, message = "Product name must be at least 3 characters")
    private String name;

    @NotBlank
    @Size(min = 6, message = "Product description must be at least 6 characters")
    private String description;
    private String image;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryModel category;
}