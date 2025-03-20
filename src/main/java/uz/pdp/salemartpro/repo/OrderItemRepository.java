package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Order;
import uz.pdp.salemartpro.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder(Order order);

    List<OrderItem> findByOrderId(Integer orderId);
}