package com.exe201.beana.controller;

import com.exe201.beana.dto.CategoryDto;
import com.exe201.beana.dto.CategoryRequestDto;
import com.exe201.beana.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(categoryId));
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid CategoryRequestDto newCategory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(newCategory));
    }

    @PutMapping("product/update/{categoryId}")
    public ResponseEntity<CategoryDto> editNameCategory(@RequestBody CategoryRequestDto categoryRequest, @PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.editNameCategory(categoryRequest, categoryId));
    }

}
