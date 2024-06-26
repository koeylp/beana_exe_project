package com.exe201.beana.controller;

import com.exe201.beana.dto.AddressDto;
import com.exe201.beana.dto.AddressRequestDto;
import com.exe201.beana.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping("")
    public ResponseEntity<AddressDto> addAddress(@RequestBody @Valid AddressRequestDto addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(addressRequest));
    }

    @GetMapping("")
    public ResponseEntity<List<AddressDto>> getAddressesByUser() {
        return ResponseEntity.ok(addressService.getAddressesByUser());
    }

    @PutMapping("/default/{addressId}")
    public ResponseEntity<AddressDto> setDefaultAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.setDefaultAddress(addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<AddressDto> deleteAddressById(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.deleteAddressById(addressId));
    }


}
