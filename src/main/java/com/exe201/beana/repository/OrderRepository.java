package com.exe201.beana.repository;

import com.exe201.beana.entity.Order;
import com.exe201.beana.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
