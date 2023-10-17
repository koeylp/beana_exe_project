package com.exe201.beana.dto;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;

@Data
public class ProductRequestFilterDto {
    @Value("${props.startPrice:0}")
    private double startPrice;

    @Value("${props.startPrice:0}")
    private double endPrice;

    private List<String> categoriesFilterList;
    private List<String> childCategoriesFilterList;
    private List<String> skinCategoriesFilterList;

    private int sortType;

    private byte status;
}
