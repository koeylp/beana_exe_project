package com.exe201.beana.service;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductEditRequestDto;
import com.exe201.beana.dto.ProductRequestDto;
import com.exe201.beana.dto.ProductRequestFilterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductDto addProduct(ProductRequestDto productRequest, HttpServletRequest request, HttpServletResponse response);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByChildCategoryId(Long childCategoryId);

    ProductDto editProduct(ProductEditRequestDto productRequest, Long productId, HttpServletRequest request, HttpServletResponse response);

    List<ProductDto> filterProductList(String sortType, String category, String childCategory, String skin, String status, String startPrice, String endPrice);

    ProductDto getProductById(Long productId);
}
