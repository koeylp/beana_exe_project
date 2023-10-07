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
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Content is required")
    @NotBlank(message = "The property is not null or whitespace")
    private String content;

    private Date timeCreated;

    private Date updatedDatetime;

    @NotEmpty(message = "Status is required")
    @NotBlank(message = "The property is not null or whitespace")
    private byte status;

    @NotEmpty(message = "UserId is required")
    private UserDto user;

    @NotEmpty(message = "ProductId is required")
    private ProductDto product;
}
