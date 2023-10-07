package com.exe201.beana.controller;

import com.exe201.beana.dto.ChildCategoryDto;
import com.exe201.beana.dto.ChildCategoryRequestDto;
import com.exe201.beana.service.ChildCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/childcategories")
public class ChildCategoryController {

    private final ChildCategoryService childCategoryService;

    @PostMapping("")
    public ResponseEntity<ChildCategoryDto> addChildCategory(@RequestBody @Valid ChildCategoryRequestDto newChildCategory) {
        return ResponseEntity.status(HttpStatus.CREATED).body(childCategoryService.addChildCategory(newChildCategory));
    }
}
