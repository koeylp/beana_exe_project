package com.exe201.beana.mapper;

import com.exe201.beana.dto.OrderDto;
import com.exe201.beana.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);
}
