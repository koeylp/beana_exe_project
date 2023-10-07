package com.exe201.beana.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    @NotEmpty(message = "Quantity is required")
    @NotBlank(message = "The property is not null or whitespace")
    private int quantity;

    @NotEmpty(message = "Price is required")
    @NotBlank(message = "The property is not null or whitespace")
    private double price;

    @NotEmpty(message = "Description is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String description;

    private String mainFunction;

    private String ingredients;

    private String specification;

    private int soldQuantity;

    private float rate;

    private String howToUse;

    private byte status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date timeCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date updatedDatetime;

    private ChildCategoryDto childCategory;

    private List<ProductSkinDto> productSkins;

    private List<CommentDto> comments;

    private List<ProductImageDto> productImageList;

    private ReputationDto reputation;
}
