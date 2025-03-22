package uz.pdp.salemartpro.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.salemartpro.dto.*;
import uz.pdp.salemartpro.entity.*;
import uz.pdp.salemartpro.entity.enums.DelivererStatus;
import uz.pdp.salemartpro.entity.enums.OrderStatus;
import uz.pdp.salemartpro.repo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class OrderService implements OrderServiceI{
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final GeocodingService geocodingService;
    private final AddressRepository addressRepository;
    private final RouteItemRepository routeItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final RouteRepository routeRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository, UserRepository userRepository1, GeocodingService geocodingService, AddressRepository addressRepository, RouteItemRepository routeItemRepository, DeliveryRepository deliveryRepository, RouteRepository routeRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.geocodingService = geocodingService;
        this.addressRepository = addressRepository;
        this.routeItemRepository = routeItemRepository;
        this.deliveryRepository = deliveryRepository;
        this.routeRepository = routeRepository;
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

    @Override
    public Page<OrderResponse> findOrders(OrderStatus status, String search, Pageable pageable) {
        org.springframework.data.domain.Page<Order> orders;

        if (status == null) {
            // If no status filter, get all orders
            orders = orderRepository.findAll(pageable);
        } else if (search == null || search.isEmpty()) {
            // If status filter but no search
            orders = orderRepository.findByStatus(status, pageable);
        } else {
            // If both status and search
            orders = orderRepository.findByStatusAndUserUsernameContainingIgnoreCaseOrStatusAndPhoneNumberContainingIgnoreCase(
                    status, search, status, search, pageable);
        }

        return orders.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public OrderResponse detachOrder(Integer id) {
        // Find the Order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Find the RouteItem associated with the Order
        Optional<RouteItem> routeItemOptional = routeItemRepository.findByOrder(order);

        if (routeItemOptional.isPresent()) {
            RouteItem routeItem = routeItemOptional.get();
            Route route = routeItem.getRoute();

            // Delete the RouteItem
            routeItemRepository.delete(routeItem);
            logger.info("Deleted RouteItem ID {} for Order ID {}", routeItem.getId(), order.getId());

            // Count remaining RouteItems in the Route
            int routeItemCount = routeItemRepository.countByRoute(route);
            if (routeItemCount == 0) {
                // If no RouteItems left, delete the Route
                routeRepository.delete(route);
                logger.info("Deleted Route ID {} as it has no RouteItems", route.getId());

                // Update the Delivery status to AVAILABLE
                Delivery delivery = route.getDelivery();
                delivery.setDelivererStatus(DelivererStatus.AVAILABLE);
                deliveryRepository.save(delivery);
                logger.info("Updated Delivery ID {} status to AVAILABLE", delivery.getId());
            }
        } else {
            logger.warn("No RouteItem found for Order ID {}", order.getId());
        }

        // Update the Order
        order.setIsAttached(false);
        order.setStatus(OrderStatus.NEW);
        order.setDelivery(null);

        Order updatedOrder = orderRepository.save(order);
        logger.info("Updated Order ID {}: isAttached=false, status=NEW, delivery=null", updatedOrder.getId());

        return convertToDTO(updatedOrder);
    }

    private OrderResponse convertToDTO(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setIsAttached(order.getIsAttached());
        dto.setNotes(order.getNotes());
        dto.setPhoneNumber(order.getPhoneNumber());

        // Map user if exists
        if (order.getUser() != null) {
            UserDto userDTO = new UserDto(
                    order.getUser().getUsername(),
                    order.getUser().getEmail(),
                    order.getUser().getPhone()
            );
            dto.setUser(userDTO);
        }

        // Map address if exists
        if (order.getDeliveredAddress() != null) {
            AddressDto addressDTO = new AddressDto(
                    order.getDeliveredAddress().getFullAddress(),
                    order.getDeliveredAddress().getLatitude(),
                    order.getDeliveredAddress().getLongitude()
            );
            dto.setDeliveredAddress(addressDTO);
        }

        // Map deliverer if exists
        if (order.getDelivery() != null) {
            DeliveryResponse deliveryDTO = new DeliveryResponse();
            deliveryDTO.setId(order.getDelivery().getId());
            deliveryDTO.setUsername(order.getDelivery().getUsername());
            deliveryDTO.setVehicleNumber(order.getDelivery().getVehicleNumber());
            dto.setDelivery(deliveryDTO);
        }

        return dto;
    }
}
