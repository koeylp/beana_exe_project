package com.exe201.beana.controller;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductRequestDto;
import com.exe201.beana.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductRequestDto productRequest, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productRequest, request));
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/childcategories/{childCategoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByChildCategoryId(@PathVariable Long childCategoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByChildCategoryId(childCategoryId));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductDto> editProduct(@RequestBody @Valid ProductRequestDto productRequest, @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.editProduct(productRequest, productId));
    }

}
