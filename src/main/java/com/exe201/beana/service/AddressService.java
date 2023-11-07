package com.exe201.beana.service;

import com.exe201.beana.dto.AddressDto;
import com.exe201.beana.dto.AddressRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    AddressDto addAddress(AddressRequestDto addressRequest);

    List<AddressDto> getAddressesByUser();

    AddressDto setDefaultAddress(Long addressId);

    AddressDto deleteAddressById(Long addressId);
}
