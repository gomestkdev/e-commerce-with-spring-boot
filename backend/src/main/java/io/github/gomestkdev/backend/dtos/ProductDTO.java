package io.github.gomestkdev.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String image;
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double specialPrice;
}