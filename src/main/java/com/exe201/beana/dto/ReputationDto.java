package com.exe201.beana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReputationDto {

    private Long id;

    private String name;

    private Date timeCreated;

    private Date updatedDatetime;

    private byte status;
}
