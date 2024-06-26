package com.exe201.beana.service;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductEditRequestDto;
import com.exe201.beana.dto.ProductRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductDto addProduct(ProductRequestDto productRequest);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByChildCategoryId(Long childCategoryId);

    ProductDto editProduct(ProductEditRequestDto productRequest, Long productId);

    List<ProductDto> filterProductList(String sortType, String category, String childCategory, String skin, String status, String startPrice, String endPrice);

    ProductDto getProductById(Long productId);
}
