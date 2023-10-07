package com.exe201.beana.mapper;

import com.exe201.beana.dto.ReputationDto;
import com.exe201.beana.entity.Reputation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReputationMapper {
    ReputationMapper INSTANCE = Mappers.getMapper(ReputationMapper.class);

    ReputationDto toReputationDto(Reputation reputation);

    Reputation toReputation(ReputationDto reputationDto);
}
