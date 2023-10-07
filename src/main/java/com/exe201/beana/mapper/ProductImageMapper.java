package com.exe201.beana.mapper;

import com.exe201.beana.dto.ProductImageDto;
import com.exe201.beana.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductImageMapper {
    ProductImageMapper INSTANCE = Mappers.getMapper(ProductImageMapper.class);

    ProductImageDto toProductImageDto(ProductImage productImage);

    ProductImage toProductImage(ProductImageDto productImageDto);
}
