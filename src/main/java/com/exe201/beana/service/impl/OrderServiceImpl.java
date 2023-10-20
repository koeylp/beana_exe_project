package com.exe201.beana.service.impl;

import com.exe201.beana.dto.OrderDetailsDto;
import com.exe201.beana.dto.OrderDto;
import com.exe201.beana.dto.OrderRequestDto;
import com.exe201.beana.entity.*;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.OrderMapper;
import com.exe201.beana.mapper.ProductMapper;
import com.exe201.beana.repository.*;
import com.exe201.beana.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public OrderDto addOrder(OrderRequestDto orderRequestDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);

        Optional<Address> foundAddress = addressRepository.findAddressByStatusAndId((byte) 1, orderRequestDto.getAddressId());
        if (foundAddress.isEmpty())
            throw new ResourceNotFoundException("Address not found with Id: " + orderRequestDto.getAddressId());

        Optional<Payment> foundPayment = paymentRepository.findPaymentByStatusAndId((byte) 1, orderRequestDto.getPaymentId());
        if (foundPayment.isEmpty())
            throw new ResourceNotFoundException("Payment not found with id: " + orderRequestDto.getPaymentId());

        Order newOrder = new Order(null, null, orderRequestDto.getAmount(), (byte) 1, foundUser.get(), foundAddress.get(), foundPayment.get(), null);
        Order tempOrder = orderRepository.save(newOrder);
        List<OrderDetailsDto> tempOrderDetailsList = new ArrayList<>();
        for (int i = 0; i < orderRequestDto.getOrderDetailsList().size(); i++) {
            Long currentProductId = orderRequestDto.getOrderDetailsList().get(i).getProductId();
            Optional<Product> foundProduct = productRepository.findProductByStatusAndId((byte) 1, currentProductId);
            if (foundProduct.isEmpty())
                throw new ResourceNotFoundException("Product not found with id: " + currentProductId);
            foundProduct.get().setSoldQuantity(foundProduct.get().getSoldQuantity() + orderRequestDto.getOrderDetailsList().get(i).getQuantity());
            foundProduct.get().setQuantity(foundProduct.get().getSoldQuantity() - orderRequestDto.getOrderDetailsList().get(i).getQuantity());
            tempOrderDetailsList.add(new OrderDetailsDto(null, orderRequestDto.getOrderDetailsList().get(i).getQuantity(), null, (byte) 1, OrderMapper.INSTANCE.toOrderDto(tempOrder), ProductMapper.INSTANCE.toProductDto(foundProduct.get())));
        }

        for (OrderDetailsDto orderDetailsDto : tempOrderDetailsList) {
            OrderDetails newOrderDetails = new OrderDetails();
            newOrderDetails.setQuantity(orderDetailsDto.getQuantity());
            newOrderDetails.setOrder(OrderMapper.INSTANCE.toOrder(orderDetailsDto.getOrderDto()));
            newOrderDetails.setStatus((byte) 1);
            newOrderDetails.setProduct(ProductMapper.INSTANCE.toProduct(orderDetailsDto.getProduct()));
            orderDetailsRepository.save(newOrderDetails);
        }
        tempOrder.setOrderDetailsList(orderDetailsRepository.getOrderDetailsByStatusAndOrder((byte) 1, tempOrder));
        return OrderMapper.INSTANCE.toOrderDto(orderRepository.save(tempOrder));
    }

    @Override
    public List<OrderDto> getOrdersByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);
        return orderRepository.findAllByStatusAndUser((byte) 1, foundUser.get()).stream().map(OrderMapper.INSTANCE::toOrderDto).collect(Collectors.toList());
    }
}
