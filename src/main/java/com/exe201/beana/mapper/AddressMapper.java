package com.exe201.beana.mapper;

import com.exe201.beana.dto.AddressDto;
import com.exe201.beana.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDto toAddressDto(Address address);

    Address toAddress(AddressDto addressDto);
}
