package com.exe201.beana.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemDto {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int quantity;

    @NotNull
    private double price;

    @NotNull
    private int cartQuantity;

    @NotNull
    private List<ProductImageDto> productImageList;

}
