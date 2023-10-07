package com.exe201.beana.mapper;

import com.exe201.beana.dto.CategoryDto;
import com.exe201.beana.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "childCategories", target = "childCategoryDtos")
    CategoryDto toCategoryDto(Category category);

    Category toCategory(CategoryDto categoryDto);
}
