package uz.pdp.salemartpro.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.salemartpro.service.OrderItemServiceI;



@RestController
@RequestMapping("/api/orderitems")
public class OrderItemController {

    private final OrderItemServiceI orderItemServiceI;

    public OrderItemController(OrderItemServiceI orderItemServiceI) {
        this.orderItemServiceI = orderItemServiceI;
    }


    @GetMapping("/{orderId}")
    public HttpEntity<?> getOrderItemsByOrderId(@PathVariable Integer orderId) {
        return orderItemServiceI.getOrderItemsByOrderId(orderId);
    }
}
