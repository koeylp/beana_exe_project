package com.exe201.beana.repository;

import com.exe201.beana.entity.Order;
import com.exe201.beana.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> getOrderDetailsByStatusAndOrder(byte status, Order order);
}
