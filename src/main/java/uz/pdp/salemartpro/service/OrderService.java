package uz.pdp.salemartpro.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.salemartpro.dto.OrderRequestDto;
import uz.pdp.salemartpro.entity.*;
import uz.pdp.salemartpro.entity.enums.OrderStatus;
import uz.pdp.salemartpro.repo.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService implements OrderServiceI{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final GeocodingService geocodingService;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository, UserRepository userRepository1, GeocodingService geocodingService, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.geocodingService = geocodingService;
        this.addressRepository = addressRepository;
    }

    @Override
    public HttpEntity<?> getAllOrders(String username) {
        List<Order> orders = orderRepository.findByUserUsername(username);
        if (orders.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        else{
            return ResponseEntity.ok().body(orders);
        }
    }

    @Override
    public HttpEntity<?> getOrderItems(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            Optional<List<OrderItem>> orderItems = order.map(orderItemRepository::findByOrder);
            if (orderItems.isPresent()){
                return ResponseEntity.ok().body(orderItems.get());
            }
            else return ResponseEntity.status(204).build();
        }
        else return ResponseEntity.status(404).build();
    }

    @Transactional
    public HttpEntity<?> receiveBasket(String username, OrderRequestDto requestDto) {
        Address address = new Address();
        address.setLatitude(requestDto.getAddress().getLatitude());
        address.setLongitude(requestDto.getAddress().getLongitude());
        address.setIsAdmin(false);
        address.setFullAddress(geocodingService.getFullAddress(requestDto.getAddress().getLatitude(), requestDto.getAddress().getLongitude()));
        addressRepository.save(address);



        User user = userRepository.findByUsername(username).orElseThrow();
        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setDeliveredAddress(address);
        order.setPhoneNumber(requestDto.getPhoneNumber());
        order.setNotes(requestDto.getNotes());
        order = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        for (Map.Entry<Integer, Integer> each : requestDto.getBasket().entrySet()) {
            Optional<Product> product = productRepository.findById(each.getKey());

            if (product.isEmpty()) {
                return ResponseEntity.status(404).body("Product not found for ID: " + each.getKey());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product.get());
            orderItem.setQuantity(each.getValue());
            orderItem.setPrice(product.get().getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        return ResponseEntity.status(201).build();
    }

}
