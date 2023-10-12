package com.exe201.beana.controller;

import com.exe201.beana.dto.PaymentDto;
import com.exe201.beana.dto.PaymentRequestDto;
import com.exe201.beana.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("")
    public ResponseEntity<PaymentDto> addPayment(@RequestBody @Valid PaymentRequestDto paymentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.addPayment(paymentRequest));
    }
}
