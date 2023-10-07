package com.exe201.beana.mapper;

import com.exe201.beana.dto.PaymentDto;
import com.exe201.beana.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentDto toPaymentDto(Payment payment);

    Payment toPayment(PaymentDto paymentDto);
}
