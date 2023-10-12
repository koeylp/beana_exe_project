package com.exe201.beana.service;

import com.exe201.beana.dto.AddressDto;
import com.exe201.beana.dto.AddressRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    AddressDto addAddress(AddressRequestDto addressRequest);
}
