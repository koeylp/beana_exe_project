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
public class ItemRequestDto {
    @NotNull
    private Long id;
    private String name;
    private int quantity;
    private double price;
    private int cartQuantity;
    private List<ProductImageDto> productImageList;
}
