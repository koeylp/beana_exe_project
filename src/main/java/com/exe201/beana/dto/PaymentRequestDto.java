package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestDto {
    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;
}
