package com.exe201.beana.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class ProductEditRequestDto {

    private String name;

    @Value("${props.quantity:-1}")
    private int quantity;

    @Value("${props.price:-1}")
    private double price;

    private String description;

    private String mainFunction;

    private String ingredients;

    private Long[] skinIds;

    private Long reputationId;

    private String specification;

    private String certification;

    private Long childCategoryId;

    private String howToUse;

    private ProductImageRequestDto[] images;
}
