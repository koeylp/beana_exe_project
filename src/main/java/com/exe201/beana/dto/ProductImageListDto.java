package com.exe201.beana.dto;

import com.exe201.beana.entity.ProductImage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductImageListDto {
    private List<ProductImage> productImageList;
    public void addItem(ProductImage item) {
        if (productImageList == null)
            productImageList = new ArrayList<>();
        productImageList.add(item);
    }
}
