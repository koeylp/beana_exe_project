package com.exe201.beana.controller;

import com.exe201.beana.dto.OrderDto;
import com.exe201.beana.dto.OrderRequestDto;
import com.exe201.beana.repository.OrderDetailsRepository;
import com.exe201.beana.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<OrderDto> addOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(orderRequestDto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByUserId(userId));
    }
}
