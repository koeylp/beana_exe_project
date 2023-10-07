package com.exe201.beana.mapper;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toProductDto(Product product);

    Product toProduct(ProductDto productDto);
}
