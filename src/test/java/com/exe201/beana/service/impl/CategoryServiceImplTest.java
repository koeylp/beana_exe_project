package com.exe201.beana.service.impl;

import com.exe201.beana.dto.CategoryDto;
import com.exe201.beana.repository.CategoryRepository;
import com.exe201.beana.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @Test
    void testGetAllCategories() {
//
//        List<CategoryDto> result = categoryService.getAllCategories();
//
//        verify(categoryRepository).findAllByStatus((byte) 1);
//
//        assertThat(result).isNotNull();
    }

    @Test
    void testAddCategory() {
    }

    @Test
    void testGetCategoryById() {

    }
}