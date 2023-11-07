package com.exe201.beana.service;

import com.exe201.beana.dto.OrderDto;
import com.exe201.beana.dto.OrderRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderService {
    OrderDto addOrder(OrderRequestDto orderRequestDto);

    List<OrderDto> getOrdersByUser();

    List<OrderDto> getOrdersForAdmin();
}
