package com.exe201.beana.repository;

import com.exe201.beana.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findPaymentByStatusAndId(byte status, Long id);

    Optional<Payment> findPaymentByStatusAndName(byte status, String name);
}
