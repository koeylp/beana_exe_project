package com.exe201.beana.service;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductDto addProduct(ProductRequestDto productRequest, HttpServletRequest request);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByChildCategoryId(Long childCategoryId);

    ProductDto editProduct(ProductRequestDto productRequest, Long productId);
}
