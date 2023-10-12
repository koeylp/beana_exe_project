package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressRequestDto {
    @NotEmpty(message = "FullName is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String fullName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Wrong phone's format")
    @NotBlank(message = "The property is not null or whitespace")
    @NotEmpty(message = "Phone is required")
    private String phone;

    @NotEmpty(message = "Province is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String province;

    @NotEmpty(message = "District is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String district;

    @NotEmpty(message = "Ward is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String ward;

    @NotEmpty(message = "Address is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String address;

    @NotNull(message = "userId is required")
    private Long userId;
}
