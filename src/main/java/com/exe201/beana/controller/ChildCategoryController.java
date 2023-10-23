package com.exe201.beana.controller;

import com.exe201.beana.dto.ChildCategoryDto;
import com.exe201.beana.dto.ChildCategoryRequestDto;
import com.exe201.beana.service.ChildCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/child-categories")
public class ChildCategoryController {

    private final ChildCategoryService childCategoryService;

    @PostMapping("")
    public ResponseEntity<ChildCategoryDto> addChildCategory(@RequestBody @Valid ChildCategoryRequestDto newChildCategory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(childCategoryService.addChildCategory(newChildCategory));
    }

    @GetMapping("")
    public ResponseEntity<List<ChildCategoryDto>> getAllChildCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(childCategoryService.getAllChildCategories());
    }
}
