package uz.pdp.salemartpro.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import uz.pdp.salemartpro.dto.OrderRequestDto;
import uz.pdp.salemartpro.dto.OrderResponse;
import uz.pdp.salemartpro.entity.enums.OrderStatus;

public interface OrderServiceI {
    HttpEntity<?> getAllOrders(String username);
    HttpEntity<?> getOrderItems(Integer orderId);
    HttpEntity<?> receiveBasket(String Username, OrderRequestDto orderRequestDto);

    // Use Spring Data's Page class
    Page<OrderResponse> findOrders(OrderStatus status, String search, Pageable pageable);
    OrderResponse detachOrder(Integer id);
}