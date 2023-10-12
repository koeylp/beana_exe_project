package com.exe201.beana.controller;

import com.exe201.beana.dto.AddressDto;
import com.exe201.beana.dto.AddressRequestDto;
import com.exe201.beana.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("")
    public ResponseEntity<AddressDto> addAddress(@RequestBody @Valid AddressRequestDto addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(addressRequest));
    }


}
