package com.exe201.beana.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailsRequestDto {
    @NotNull(message = "productId is null")
    private Long productId;

    @NotNull(message = "quantity is null")
    private int quantity;
}
