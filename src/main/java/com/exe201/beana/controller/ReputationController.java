package com.exe201.beana.controller;

import com.exe201.beana.dto.ReputationDto;
import com.exe201.beana.dto.ReputationRequestDto;
import com.exe201.beana.service.ReputationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/reputations")
public class ReputationController {

    private final ReputationService reputationService;

    @GetMapping("")
    public ResponseEntity<List<ReputationDto>> getAllReputations() {
        return ResponseEntity.status(HttpStatus.OK).body(reputationService.getAllReputations());
    }

    @PostMapping("")
    public ResponseEntity<ReputationDto> addReputation(@RequestBody @Valid ReputationRequestDto newReputation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reputationService.addReputation(newReputation));
    }
}
