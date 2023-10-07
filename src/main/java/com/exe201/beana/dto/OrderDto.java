package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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

    @NotEmpty(message = "Status is required")
    @NotBlank(message = "The property is not null or whitespace")
    private double amount;

    private Date timeCreated;

    private Date updatedDatetime;

    @NotEmpty(message = "Status is required")
    @NotBlank(message = "The property is not null or whitespace")
    private byte status;

    @NotEmpty(message = "UserId is required")
    @NotBlank(message = "The property is not null or whitespace")
    private UserDto user;

    @NotEmpty(message = "AddressId is required")
    @NotBlank(message = "The property is not null or whitespace")
    private AddressDto address;

    @NotEmpty(message = "PaymentId is required")
    @NotBlank(message = "The property is not null or whitespace")
    private PaymentDto payment;

    @NotEmpty(message = "OrderDetails is required")
    private List<OrderDetailsDto> orderDetailsList;
}
