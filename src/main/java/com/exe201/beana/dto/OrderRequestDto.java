package com.exe201.beana.dto;

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
public class OrderRequestDto {

    @NotNull(message = "Amount is required")
    private double amount;

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotNull(message = "AddressId is required")
    private Long addressId;

    @NotNull(message = "PaymentId is required")
    private Long paymentId;

    @NotNull(message = "orderItemIds is required")
    private List<OrderDetailsRequestDto> orderDetailsList;
}
