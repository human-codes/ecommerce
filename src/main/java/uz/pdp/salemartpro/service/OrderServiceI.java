package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import uz.pdp.salemartpro.dto.OrderRequestDto;

import java.util.Map;

public interface OrderServiceI {
    HttpEntity<?> getAllOrders(String username);

    HttpEntity<?> getOrderItems(Integer orderId);

    HttpEntity<?> receiveBasket(String Username, OrderRequestDto orderRequestDto);

}
