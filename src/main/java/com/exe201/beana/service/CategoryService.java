package com.exe201.beana.service;

import com.exe201.beana.dto.CategoryDto;
import com.exe201.beana.dto.CategoryRequestDto;
import com.exe201.beana.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories();

    CategoryDto addCategory(CategoryRequestDto newCategory);

    CategoryDto getCategoryById(Long categoryId);

    CategoryDto editNameCategory(CategoryRequestDto categoryRequest, Long categoryId);
}
