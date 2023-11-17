package com.exe201.beana.service;

import com.exe201.beana.dto.OrderDto;
import com.exe201.beana.dto.OrderRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public interface OrderService {
    OrderDto addOrder(OrderRequestDto orderRequestDto, HttpServletRequest request, HttpServletResponse response);

    List<OrderDto> getOrdersByUser();

    List<OrderDto> getOrdersForAdmin();
}
