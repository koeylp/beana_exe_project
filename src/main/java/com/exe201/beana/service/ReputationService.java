package com.exe201.beana.service;

import com.exe201.beana.dto.ReputationDto;
import com.exe201.beana.dto.ReputationRequestDto;

import java.util.List;

public interface ReputationService {
    List<ReputationDto> getAllReputations();

    ReputationDto addReputation(ReputationRequestDto newReputation);
}
