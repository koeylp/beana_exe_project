package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Quantity is required")
    @NotBlank(message = "The property is not null or whitespace")
    private int quantity;

    @NotEmpty(message = "Price is required")
    @NotBlank(message = "The property is not null or whitespace")
    private double price;

    private String description;

    private String mainFunction;

    private String ingredients;

    @NotEmpty(message = "Skin is required")
    @NotBlank(message = "The property is not null or whitespace")
    private Long[] skinIds;

    @NotEmpty(message = "Reputation is required")
    @NotBlank(message = "The property is not null or whitespace")
    private Long reputationId;

    private String specification;

    private String preservation;

    @NotEmpty(message = "ChildCategoryId is required")
    @NotBlank(message = "The property is not null or whitespace")
    private Long childCategoryId;

    private Long[] productImageIds;

    private String howToUse;
}
