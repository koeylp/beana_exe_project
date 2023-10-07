package com.exe201.beana.service;

import com.exe201.beana.dto.ProductDto;
import com.exe201.beana.dto.ProductRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductDto addProduct(ProductRequestDto productRequest);

    List<ProductDto> getAllProducts();

    List<ProductDto> getProductsByChildCategoryId(Long childCategoryId);
}
