package com.exe201.beana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeAvatarRequestDto {
    @NotBlank(message = "url must be not blank")
    @NotNull(message = "url must not null")
    String url;
}
