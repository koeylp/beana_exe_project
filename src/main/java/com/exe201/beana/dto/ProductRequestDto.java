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
public class ProductRequestDto {

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    @NotNull(message = "Quantity is required")
    private int quantity;

    @NotNull(message = "Price is required")
    private double price;

    private String description;

    private String mainFunction;

    private String ingredients;

    @NotNull(message = "skinIds not found")
    private Long[] skinIds;

    @NotNull(message = "reputationId not found")
    private Long reputationId;

    private String specification;

    private String certification;

    @NotNull(message = "childCategoryId not found")
    private Long childCategoryId;

    private Long[] productImageIds;

    private String howToUse;
}
