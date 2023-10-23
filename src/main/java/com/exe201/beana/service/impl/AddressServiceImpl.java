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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressDto addAddress(AddressRequestDto addressRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);
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
        List<Address> addresses = addressRepository.findAllByUser(foundUser.get());
        for (Address address : addresses) {
            if (address.getStatus() == 1)
                address.setStatus((byte) 0);
        }
        return AddressMapper.INSTANCE.toAddressDto(addressRepository.save(newAddress));
    }

    @Override
    public List<AddressDto> getAddressesByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);
        return addressRepository.findAllByUser(
                        foundUser.get()).stream().map(AddressMapper.INSTANCE::toAddressDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto setDefaultAddress(Long addressId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);

        Optional<Address> defaultAddress = addressRepository.findByStatusAndUser((byte) 1, foundUser.get());
        if (defaultAddress.isEmpty())
            throw new ResourceNotFoundException("Default address not found");
        defaultAddress.get().setStatus((byte) 0);
        addressRepository.save(defaultAddress.get());

        Optional<Address> foundAddress = addressRepository.findByIdAndUser(addressId, foundUser.get());
        if (foundAddress.isEmpty())
            throw new ResourceNotFoundException("Address Not found with id: " + addressId + " for username: " + username);
        foundAddress.get().setStatus((byte) 1);
        return AddressMapper.INSTANCE.toAddressDto(addressRepository.save(foundAddress.get()));
    }
}
