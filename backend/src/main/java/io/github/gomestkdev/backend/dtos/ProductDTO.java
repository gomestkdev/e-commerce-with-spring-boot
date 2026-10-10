package io.github.gomestkdev.backend.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, message = "Product name must be at least 3 characters")
    private String name;

    @NotBlank(message = "Product description cannot be blank")
    @Size(min = 6, message = "Product description must be at least 6 characters")
    private String description;

    private String image;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private Double discount;
    private Double specialPrice;
}