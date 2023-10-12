package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDto {
    private Long id;

    @NotEmpty(message = "name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    @NotNull(message = "quantity is required")
    private int quantity;

    @NotNull(message = "price is required")
    private double price;

    @NotNull(message = "cartQuantity is required")
    private int cartQuantity;

}
