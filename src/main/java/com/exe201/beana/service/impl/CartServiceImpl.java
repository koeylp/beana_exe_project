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
import java.util.List;
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
        cart.updateQuantity(item, type);
        System.out.println(cart.getItems().get(0).getQuantity());
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
        cartCookie.setMaxAge(24 * 60 * 60 * 1000);
        cartCookie.setSecure(true);
        response.addCookie(cartCookie);
    }

//    public void saveCartToCookie(CartDto cart, HttpServletRequest request, HttpServletResponse response) {
//        Cookie[] cookies = request.getCookies();
//
//        // Check if the cart cookie already exists
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(CART_COOKIE_NAME)) {
//                    // Create a new cookie with the updated value
//                    Cookie updatedCookie = new Cookie(CART_COOKIE_NAME, serializeCart(cart));
//                    updatedCookie.setMaxAge(24 * 60 * 60 * 1000);
//                    updatedCookie.setSecure(true);
//                    response.addCookie(updatedCookie);
//                    return; // Exit the method after updating the existing cookie
//                }
//            }
//        }
//
//        // If the cart cookie doesn't exist, create a new one
//        Cookie cartCookie = new Cookie(CART_COOKIE_NAME, serializeCart(cart));
//        cartCookie.setMaxAge(24 * 60 * 60 * 1000);
//        cartCookie.setSecure(true);
//        response.addCookie(cartCookie);
//    }

    private void saveCartToCookie(CartDto cart, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        // Create a new list to hold the updated cookies
        List<Cookie> updatedCookies = new ArrayList<>();

        // Check if the cart cookie already exists
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CART_COOKIE_NAME)) {
                    // Update the existing cookie with the new serialized cart
                    cookie.setValue(serializeCart(cart));
                    cookie.setMaxAge(24 * 60 * 60 * 1000);
                    cookie.setSecure(true);
                }
                updatedCookies.add(cookie);
            }
        }

        // If the cart cookie doesn't exist, create a new one
        if (!containsCookie(updatedCookies, CART_COOKIE_NAME)) {
            Cookie cartCookie = new Cookie(CART_COOKIE_NAME, serializeCart(cart));
            cartCookie.setMaxAge(24 * 60 * 60 * 1000);
            cartCookie.setSecure(true);
            updatedCookies.add(cartCookie);
        }

        // Set the updated cookies in the response
        for (Cookie updatedCookie : updatedCookies) {
            response.addCookie(updatedCookie);
        }
    }

    // Helper method to check if a cookie with a specific name exists in the list
    private boolean containsCookie(List<Cookie> cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return true;
            }
        }
        return false;
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
