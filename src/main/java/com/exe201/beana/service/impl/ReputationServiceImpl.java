package com.exe201.beana.service.impl;

import com.exe201.beana.dto.ReputationDto;
import com.exe201.beana.dto.ReputationRequestDto;
import com.exe201.beana.entity.Reputation;
import com.exe201.beana.exception.ResourceNameAlreadyExistsException;
import com.exe201.beana.mapper.ReputationMapper;
import com.exe201.beana.repository.ReputationRepository;
import com.exe201.beana.service.ReputationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReputationServiceImpl implements ReputationService {

    private final ReputationRepository reputationRepository;

    @Override
    public List<ReputationDto> getAllReputations() {
        return reputationRepository.findAllByStatus((byte) 1).stream().map(ReputationMapper.INSTANCE::toReputationDto).collect(Collectors.toList());
    }

    @Override
    public ReputationDto addReputation(ReputationRequestDto newReputation) {
        Optional<Reputation> foundReputation = reputationRepository.findReputationByStatusAndName((byte) 1, newReputation.getName());
        if (foundReputation.isPresent()) {
            throw new ResourceNameAlreadyExistsException("Reputation already exist with id: " + foundReputation.get().getId());
        }
        Reputation addedReputation = new Reputation();
        addedReputation.setName(newReputation.getName());
        addedReputation.setStatus((byte) 1);
        return ReputationMapper.INSTANCE.toReputationDto(reputationRepository.save(addedReputation));
    }
}
