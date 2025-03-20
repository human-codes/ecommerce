package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.entity.OrderItem;
import uz.pdp.salemartpro.repo.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService implements OrderItemServiceI {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public HttpEntity<?> getOrderItemsByOrderId(Integer orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        if (orderItems.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        else return ResponseEntity.ok().body(orderItems);
    }
}
