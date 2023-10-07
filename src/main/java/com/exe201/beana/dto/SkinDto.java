package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SkinDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    private Date timeCreated;

    private Date updatedDatetime;

    private byte status;
}
