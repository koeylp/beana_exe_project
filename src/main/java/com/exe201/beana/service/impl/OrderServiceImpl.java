package com.exe201.beana.service.impl;

import com.exe201.beana.dto.CartDto;
import com.exe201.beana.dto.OrderDetailsDto;
import com.exe201.beana.dto.OrderDto;
import com.exe201.beana.dto.OrderRequestDto;
import com.exe201.beana.entity.*;
import com.exe201.beana.exception.AccessDeniedException;
import com.exe201.beana.exception.ResourceNotFoundException;
import com.exe201.beana.mapper.OrderMapper;
import com.exe201.beana.mapper.ProductMapper;
import com.exe201.beana.repository.*;
import com.exe201.beana.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;

    private final static String CART_COOKIE_NAME = "CART_COOKIE";

    @Override
    public OrderDto addOrder(OrderRequestDto orderRequestDto, HttpServletRequest request, HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);

        Optional<Address> foundAddress = addressRepository.findById(orderRequestDto.getAddressId());
        if (foundAddress.isEmpty())
            throw new ResourceNotFoundException("Address not found with id: " + orderRequestDto.getAddressId());

        Optional<Payment> foundPayment = paymentRepository.findPaymentByStatusAndId((byte) 1, orderRequestDto.getPaymentId());
        if (foundPayment.isEmpty())
            throw new ResourceNotFoundException("Payment not found with id: " + orderRequestDto.getPaymentId());

        Order newOrder = new Order(null, null, orderRequestDto.getAmount(), (byte) 1, foundUser.get(), foundAddress.get(), foundPayment.get(), null);
        Order tempOrder = new Order();

        List<OrderDetailsDto> tempOrderDetailsList = new ArrayList<>();
        for (int i = 0; i < orderRequestDto.getOrderDetailsList().size(); i++) {
            Long currentProductId = orderRequestDto.getOrderDetailsList().get(i).getProductId();
            Optional<Product> foundProduct = productRepository.findProductByStatusAndId((byte) 1, currentProductId);
            if (foundProduct.isEmpty())
                throw new ResourceNotFoundException("Product not found with id: " + currentProductId);

            // check quantity
            if (foundProduct.get().getQuantity() - orderRequestDto.getOrderDetailsList().get(i).getQuantity() < 0)
                throw new AccessDeniedException("The quantity of product with id " + orderRequestDto.getOrderDetailsList().get(i).getProductId()
                        + "must less than or equal to " + foundProduct.get().getQuantity());

            // edit quantity when the condition of quantity is checked well
            foundProduct.get().setSoldQuantity(foundProduct.get().getSoldQuantity() + orderRequestDto.getOrderDetailsList().get(i).getQuantity());
            foundProduct.get().setQuantity(foundProduct.get().getQuantity() - orderRequestDto.getOrderDetailsList().get(i).getQuantity());

            // if out of stock set status to 0
            if (foundProduct.get().getQuantity() == 0)
                foundProduct.get().setStatus((byte) 0);

            tempOrder = orderRepository.save(newOrder);

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

        CartDto cartDto = getCartFromCookie(request);
        if (cartDto.getItems() != null)
            cartDto.clearCart();
        saveCartToCookie(cartDto, request, response);

        return OrderMapper.INSTANCE.toOrderDto(orderRepository.save(tempOrder));
    }

    private void saveCartToCookie(CartDto cart, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CART_COOKIE_NAME)) {
                    cookie.setValue(serializeCart(cart));
                    cookie.setMaxAge(1);
                    cookie.setSecure(true);
                    response.addCookie(cookie);
                }
            }
        }

    }

    private String serializeCart(CartDto cart) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String cartJson = objectMapper.writeValueAsString(cart);
            return URLEncoder.encode(cartJson, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private CartDto getCartFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CART_COOKIE_NAME)) {
                    return deserializeCart(cookie.getValue());
                }
            }
        }
        return new CartDto();
    }

    private CartDto deserializeCart(String cartJson) {
        try {
            String decodedCartData = URLDecoder.decode(cartJson, StandardCharsets.UTF_8);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decodedCartData, CartDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderDto> getOrdersByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> foundUser = userRepository.findUserByStatusAndUsername((byte) 1, username);
        if (foundUser.isEmpty())
            throw new ResourceNotFoundException("User Not found with username: " + username);

        List<OrderDto> orderList = new ArrayList<>(orderRepository.findAllByUser(foundUser.get()).stream().map(OrderMapper.INSTANCE::toOrderDto).toList());

        orderList.sort(Comparator.comparing(OrderDto::getOrderDate).reversed());

        return orderList;
    }

    @Override
    public List<OrderDto> getOrdersForAdmin() {
        List<OrderDto> orderList = new ArrayList<>(new ArrayList<>(orderRepository.findAll()).stream().map(OrderMapper.INSTANCE::toOrderDto).toList());
        orderList.sort(Comparator.comparing(OrderDto::getOrderDate).reversed());
        return orderList;
    }

    @Override
    public OrderDto updateStatus(Long orderId, byte newStatus) {
        Optional<Order> foundOrder = orderRepository.findById(orderId);
        if (foundOrder.isEmpty())
            throw new ResourceNotFoundException("Order Not found with username: " + orderId);

        foundOrder.get().setStatus(newStatus);
        return OrderMapper.INSTANCE.toOrderDto(orderRepository.save(foundOrder.get()));
    }


}
