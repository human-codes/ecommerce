package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;

public interface OrderItemServiceI {
    HttpEntity<?> getOrderItemsByOrderId(Integer orderId);
}
