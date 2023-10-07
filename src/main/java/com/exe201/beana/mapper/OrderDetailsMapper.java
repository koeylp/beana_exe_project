package com.exe201.beana.mapper;

import com.exe201.beana.dto.OrderDetailsDto;
import com.exe201.beana.entity.OrderDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailsMapper {
    OrderDetailsMapper INSTANCE = Mappers.getMapper(OrderDetailsMapper.class);

    OrderDetailsDto toOrderDetailsDto(OrderDetails orderDetails);

    OrderDetails toOrderDetails(OrderDetailsDto orderDetailsDto);
}
