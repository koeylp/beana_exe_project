package com.exe201.beana.service.impl;

import com.exe201.beana.dto.CategoryDto;
import com.exe201.beana.dto.CategoryRequestDto;
import com.exe201.beana.entity.Category;
import com.exe201.beana.exception.ResourceNameAlreadyExistsException;
import com.exe201.beana.mapper.CategoryMapper;
import com.exe201.beana.repository.CategoryRepository;
import com.exe201.beana.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAllByStatus((byte) 1).stream().map(CategoryMapper.INSTANCE::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto addCategory(CategoryRequestDto newCategory) {
        Optional<Category> foundCategory = categoryRepository.findCategoryByStatusAndName((byte) 1, newCategory.getName());
        if (foundCategory.isPresent()) {
            throw new ResourceNameAlreadyExistsException("Category already exists with id: " + foundCategory.get().getId());
        }
        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName(newCategory.getName());
        newCategoryDto.setStatus((byte) 1);
        Category addedCategory = categoryRepository.save(CategoryMapper.INSTANCE.toCategory(newCategoryDto));
        return CategoryMapper.INSTANCE.toCategoryDto(addedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Optional<Category> foundCategory = categoryRepository.findCategoryByStatusAndId((byte) 1, categoryId);
        if (foundCategory.isEmpty()) {
            throw new ResourceNameAlreadyExistsException("Can not find category with id: " + categoryId);
        }
        return CategoryMapper.INSTANCE.toCategoryDto(foundCategory.get());
    }

    @Override
    public CategoryDto editNameCategory(CategoryRequestDto categoryRequest, Long categoryId) {
        Optional<Category> foundCategory = categoryRepository.findCategoryByStatusAndId((byte) 1, categoryId);
        if (foundCategory.isEmpty())
            throw new ResourceNameAlreadyExistsException("Can not find category with id: " + categoryId);
        foundCategory.get().setName(categoryRequest.getName());
        return CategoryMapper.INSTANCE.toCategoryDto(categoryRepository.save(foundCategory.get()));
    }

}
