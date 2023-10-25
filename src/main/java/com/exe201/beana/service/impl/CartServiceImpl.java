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

import java.io.UnsupportedEncodingException;
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
        if (cart.getItems() != null) {
            for (CartItemDto cartItem : cart.getItems()) {
                if (Objects.equals(cartItem.getItem().getId(), item.getId())) {
                    cartItem.setQuantity(cartItem.getQuantity() + item.getCartQuantity());
                    saveCartToCookie(cart, response);
                    return;
                }
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
        cart.removeItem(item);
        saveCartToCookie(cart, response);
    }

    @Override
    public void updateQuantity(ItemDto item, String type, HttpServletRequest request, HttpServletResponse response) {
        CartDto cart = getCart(request);
        for (CartItemDto cartItemDto : cart.getItems()) {
            if (Objects.equals(cartItemDto.getItem().getId(), item.getId())) {
                if (type.equalsIgnoreCase("minus"))
                    cartItemDto.setQuantity(cartItemDto.getQuantity() - 1);
                else if (type.equalsIgnoreCase("plus"))
                    cartItemDto.setQuantity(cartItemDto.getQuantity() - 1);
            }
        }
        saveCartToCookie(cart, request, response);
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
        System.out.println(cartCookie.getValue());
        cartCookie.setMaxAge(24 * 60 * 60 * 1000);
//        cartCookie.setSecure(true);
        response.addCookie(cartCookie);
    }

    public void saveCartToCookie(CartDto cart, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String serializedCart = serializeCart(cart);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CART_COOKIE_NAME)) {
                    cookie.setValue(serializedCart);
                    cookie.setMaxAge(24 * 60 * 60); // 1 day
                    response.addCookie(cookie);
                    return;
                }
            }
        }
        // If the "shoppingCart" cookie doesn't exist, create a new one
        Cookie cartCookie = new Cookie("shoppingCart", serializedCart);
        cartCookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cartCookie);
    }

    //    private String serializeCart(CartDto cart) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            return URLEncoder.encode(objectMapper.writeValueAsString(cart), StandardCharsets.UTF_8);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
    private CartDto deserializeCart(String cartJson) {
        try {
            String decodedCartData = URLDecoder.decode(cartJson, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decodedCartData, CartDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String serializeCart(CartDto cart) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String cartJson = objectMapper.writeValueAsString(cart);
            return URLEncoder.encode(cartJson, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
