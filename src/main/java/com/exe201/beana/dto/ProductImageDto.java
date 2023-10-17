package com.exe201.beana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImageDto {
    private Long id;
    private String url;
    private byte status;
    private int type;
}
