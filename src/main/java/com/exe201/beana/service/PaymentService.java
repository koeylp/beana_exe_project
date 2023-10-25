package com.exe201.beana.service;

import com.exe201.beana.dto.PaymentDto;
import com.exe201.beana.dto.PaymentRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentService {
    PaymentDto addPayment(PaymentRequestDto paymentRequest);

    List<PaymentDto> getAllPayments();
}
