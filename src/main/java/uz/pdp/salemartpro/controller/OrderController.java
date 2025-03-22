package uz.pdp.salemartpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import uz.pdp.salemartpro.dto.OrderRequestDto;
import uz.pdp.salemartpro.entity.Order;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.repo.OrderRepository;
import uz.pdp.salemartpro.repo.UserRepository;
import uz.pdp.salemartpro.service.OrderServiceI;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderServiceI orderServiceI;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderServiceI orderServiceI, UserRepository userRepository, OrderRepository orderRepository) {
        this.orderServiceI = orderServiceI;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/all")
    public HttpEntity<?> getAllOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return orderServiceI.getAllOrders(userDetails.getUsername());
    }

    @GetMapping("/items/{orderId}")
    public HttpEntity<?> getOrderItems(@PathVariable Integer orderId) {
        return orderServiceI.getOrderItems(orderId);
    }

    @PostMapping()
    public HttpEntity<?> receiveBasket(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderRequestDto orderRequest) {
        return orderServiceI.receiveBasket(userDetails.getUsername(), orderRequest);
    }

    @GetMapping("/user/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Integer customerId) {
        Optional<User> userOptional = userRepository.findById(customerId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }


        List<Order> orders = orderRepository.findByUserId(customerId);
        return ResponseEntity.ok(orders);
    }




}


