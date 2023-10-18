package com.exe201.beana.service.impl;

import com.exe201.beana.dto.CartDto;
import com.exe201.beana.dto.CartItemDto;
import com.exe201.beana.dto.ItemDto;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    private final static String CART_COOKIE_NAME = "CART_COOKIE";

    @Override
    public void addItemToCart(ItemDto item, HttpServletRequest request, HttpServletResponse response) {
        CartDto cart = getCart(request);
        if (cart == null)
            cart = new CartDto();
        for (CartItemDto cartItem : cart.getItems()) {
            if (Objects.equals(cartItem.getItem().getId(), item.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + item.getCartQuantity());
                saveCartToCookie(cart, response);
                return;
            }
        }
        cart.addItem(item, item.getCartQuantity());
        saveCartToCookie(cart, response);
    }

    @Override
    public CartDto getCart(HttpServletRequest request) {
        return getCartFromCookie(request);
    }

    @Override
    public void removeItem(ItemDto item, HttpServletRequest request, HttpServletResponse response) {
        CartDto cart = getCart(request);
        if (cart.getItems() == null)
            throw new ResourceNotFoundException("Cart is null");
        if (!cart.getItems().contains(item))
            throw new ResourceNotFoundException("Item not found in cart");
        cart.removeItem(item);
        saveCartToCookie(cart, response);
    }

    private CartDto getCartFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CART_COOKIE_NAME)) {
                    return deserializeCart(cookie.getValue());
                }
            }
        }
        return new CartDto();
    }

    private void saveCartToCookie(CartDto cart, HttpServletResponse response) {
        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, serializeCart(cart));
        cartCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cartCookie);
    }

    private String serializeCart(CartDto cart) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return URLEncoder.encode(objectMapper.writeValueAsString(cart), StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private CartDto deserializeCart(String cartJson) {
        try {
            String decodedCartData = URLDecoder.decode(cartJson, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decodedCartData, CartDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
