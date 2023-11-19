package com.exe201.beana.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private Long id;

    @NotEmpty(message = "Email is required")
    @Email(message = "Wrong email's format")
    @NotBlank(message = "The property is not null or whitespace")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Username is required")
    @NotBlank(message = "The property is not null or whitespace")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone must be only number and including 10 numbers")
    @NotEmpty(message = "Phone is required")
    @NotBlank(message = "The property is not null or whitespace")
    @Column(unique = true)
    private String phone;

    private int gender;

    @NotNull(message = "Birthday is required")
    private Date dob;

    @NotEmpty(message = "Role is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String role;

    private String avatar;

    private Date timeCreated;

    private Date updatedDatetime;

    private byte status;
}
