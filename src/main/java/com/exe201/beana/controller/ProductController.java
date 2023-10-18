package com.exe201.beana.controller;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductEditRequestDto;
import com.exe201.beana.dto.ProductRequestDto;
import com.exe201.beana.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductRequestDto productRequest, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(productRequest, request, response));
    }

    @GetMapping("")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/childcategories/{childCategoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByChildCategoryId(@PathVariable Long childCategoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductsByChildCategoryId(childCategoryId));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductDto> editProduct(@RequestBody @Valid ProductEditRequestDto productRequest, @PathVariable Long productId,
                                                  HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.editProduct(productRequest, productId, request, response));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProductList(@RequestParam(name = "sort-type", required = false, defaultValue = "") String sortType,
                                                              @RequestParam(name = "category", required = false, defaultValue = "") String category,
                                                              @RequestParam(name = "child-category", required = false, defaultValue = "") String child_category,
                                                              @RequestParam(name = "skin", required = false, defaultValue = "") String skin,
                                                              @RequestParam(name = "status", required = false) String status,
                                                              @RequestParam(name = "start-price", required = false) String start_price,
                                                              @RequestParam(name = "end-price", required = false) String end_price) {
        return ResponseEntity.ok(productService.filterProductList(sortType, category, child_category, skin, status, start_price, end_price));
    }


}
