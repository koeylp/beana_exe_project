package com.exe201.beana.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDto {

    @NotEmpty(message = "Username is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String username;

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Wrong email's format")
    @NotBlank(message = "The property is not null or whitespace")
    private String email;

    @Pattern(regexp = "((84|0[3|5|7|8|9])+([0-9]{8})\\b)", message = "Phone must be start with 0 or 84 (Ex: 03, 05, 07, 08, 09 or replace 0 with 84) only number and including 10 numbers with 0 and 11 with 84")
    @NotEmpty(message = "Phone is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String phone;

    @NotNull(message = "gender is required")
    private int gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private Date dob;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "The property is not null or whitespace")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}", message = "Minimum eight characters, at least one letter and one number")
    private String password;

    @Value("false")
    private boolean admin;
}
