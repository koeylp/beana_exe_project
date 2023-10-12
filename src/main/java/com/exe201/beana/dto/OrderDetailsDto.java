package com.exe201.beana.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailsDto {

    private Long id;

    @NotNull(message = "quantity is required")
    private int quantity;

    private Date timeCreated;

    @NotNull(message = "Status is required")
    private byte status;

    @JsonIgnore
    private OrderDto orderDto;

    @NotNull(message = "ProductId is required")
    private ProductDto product;
}
