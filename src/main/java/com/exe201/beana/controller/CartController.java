package com.exe201.beana.controller;

import com.exe201.beana.dto.CartDto;
import com.exe201.beana.dto.ItemDto;
import com.exe201.beana.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addItemToCart(@RequestBody @Valid ItemDto item, HttpServletRequest request, HttpServletResponse response) {
        cartService.addItemToCart(item, request, response);
        return ResponseEntity.ok("Item added to cart.");
    }

    @GetMapping("/get")
    public ResponseEntity<CartDto> getCart(HttpServletRequest request) {
        CartDto cart = cartService.getCart(request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeItem(@RequestBody @Valid ItemDto item, HttpServletRequest request, HttpServletResponse response) {
        cartService.removeItem(item, request, response);
        return ResponseEntity.ok("Removed item.");
    }


}
