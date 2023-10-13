package com.exe201.beana.service.impl;

import com.exe201.beana.dto.PaymentDto;
import com.exe201.beana.dto.PaymentRequestDto;
import com.exe201.beana.entity.Payment;
import com.exe201.beana.exception.ResourceAlreadyExistsException;
import com.exe201.beana.mapper.PaymentMapper;
import com.exe201.beana.repository.PaymentRepository;
import com.exe201.beana.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto addPayment(PaymentRequestDto paymentRequest) {
        Optional<Payment> foundPayment = paymentRepository.findPaymentByStatusAndName((byte) 1, paymentRequest.getName());
        if (foundPayment.isPresent())
            throw new ResourceAlreadyExistsException("Payment's name already exists with id: " + foundPayment.get().getId());
        var newPayment = Payment.builder()
                .name(paymentRequest.getName())
                .status((byte) 1)
                .build();
        return PaymentMapper.INSTANCE.toPaymentDto(paymentRepository.save(newPayment));
    }
}
