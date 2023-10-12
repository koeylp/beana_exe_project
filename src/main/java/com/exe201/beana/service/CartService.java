package com.exe201.beana.service;

import com.exe201.beana.dto.CartDto;
import com.exe201.beana.dto.ItemDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
    void addItemToCart(ItemDto item, HttpServletRequest request, HttpServletResponse response);

    CartDto getCart(HttpServletRequest request);

    void removeItem(ItemDto item, HttpServletRequest request, HttpServletResponse response);
}
