package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChildCategoryDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String name;

    private Date timeCreated;

    private Date updatedDatetime;

    @NotEmpty(message = "Status is required")
    private byte status;
}
