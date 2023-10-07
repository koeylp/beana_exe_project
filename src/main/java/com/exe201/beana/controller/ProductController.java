package com.exe201.beana.controller;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductRequestDto;
import com.exe201.beana.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductRequestDto productRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/childcategories/{childCategoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByChildCategoryId(@PathVariable Long childCategoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByChildCategoryId(childCategoryId));
    }

}
