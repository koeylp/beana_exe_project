package com.exe201.beana.service;

import com.exe201.beana.dto.PaymentDto;
import com.exe201.beana.dto.PaymentRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    PaymentDto addPayment(PaymentRequestDto paymentRequest);
}
