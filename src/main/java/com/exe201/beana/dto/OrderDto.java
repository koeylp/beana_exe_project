package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {
    private Long id;

    @NotEmpty(message = "Order's date is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String orderDate;

    @NotNull(message = "Amount is required")
    private double amount;

    private byte status;

    private UserDto user;

    private AddressDto address;

    private PaymentDto payment;

    private List<OrderDetailsDto> orderDetailsList;

    private int faceScanning;

    private String code;

}
