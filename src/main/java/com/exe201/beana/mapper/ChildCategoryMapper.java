package com.exe201.beana.mapper;

import com.exe201.beana.dto.ChildCategoryDto;
import com.exe201.beana.entity.Category;
import com.exe201.beana.entity.ChildCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChildCategoryMapper {
    ChildCategoryMapper INSTANCE = Mappers.getMapper(ChildCategoryMapper.class);

    ChildCategoryDto toChildCategoryDto(ChildCategory childCategory);

    ChildCategory toChildCategory(ChildCategoryDto childCategoryDto);
}
