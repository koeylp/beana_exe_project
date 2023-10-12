package com.exe201.beana.service.impl;

import com.exe201.beana.dto.AddressDto;
import com.exe201.beana.dto.AddressRequestDto;
import com.exe201.beana.entity.Address;
import com.exe201.beana.entity.User;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.AddressMapper;
import com.exe201.beana.repository.AddressRepository;
import com.exe201.beana.repository.UserRepository;
import com.exe201.beana.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressDto addAddress(AddressRequestDto addressRequest) {
        Optional<User> foundUser = userRepository.findUserByStatusAndId((byte) 1, addressRequest.getUserId());
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User not found with id: " + addressRequest.getUserId());
        var newAddress = Address.builder()
                .fullName(addressRequest.getFullName())
                .phone(addressRequest.getPhone())
                .province(addressRequest.getProvince())
                .district(addressRequest.getDistrict())
                .ward(addressRequest.getWard())
                .address(addressRequest.getAddress())
                .status((byte) 1)
                .user(foundUser.get())
                .build();
        return AddressMapper.INSTANCE.toAddressDto(addressRepository.save(newAddress));
    }
}
