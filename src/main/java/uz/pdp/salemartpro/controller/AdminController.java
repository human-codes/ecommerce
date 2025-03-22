package uz.pdp.salemartpro.controller;


import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page; // This is the correct import
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.salemartpro.dto.*;
import uz.pdp.salemartpro.entity.*;
import uz.pdp.salemartpro.entity.enums.DelivererStatus;
import uz.pdp.salemartpro.entity.enums.OrderStatus;
import uz.pdp.salemartpro.repo.DeliveryRepository;
import uz.pdp.salemartpro.repo.OrderRepository;
import uz.pdp.salemartpro.repo.RouteItemRepository;
import uz.pdp.salemartpro.repo.RouteRepository;
import uz.pdp.salemartpro.service.AdminServiceI;
import uz.pdp.salemartpro.service.DelivereyServis;
import uz.pdp.salemartpro.service.OperatorService;
import uz.pdp.salemartpro.service.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@MultipartConfig

public class AdminController {
    private final AdminServiceI adminServiceI;
    private final DelivereyServis delivereyServis;
    private final OperatorService operatorService;
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final RouteRepository routeRepository;
    private final RouteItemRepository routeItemRepository;
    private final OrderService orderService;

    public AdminController(AdminServiceI adminServiceI, DelivereyServis delivereyServis, OperatorService operatorService, DeliveryRepository deliveryRepository, OrderRepository orderRepository, RouteRepository routeRepository, RouteItemRepository routeItemRepository, OrderService orderService) {
        this.adminServiceI = adminServiceI;
        this.delivereyServis = delivereyServis;
        this.operatorService = operatorService;
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.routeRepository = routeRepository;
        this.routeItemRepository = routeItemRepository;
        this.orderService = orderService;
    }

    @DeleteMapping("/category/{id}")
    public HttpEntity<?> deleteCategory(@PathVariable int id) {
        return adminServiceI.deleteCategory(id);
    }

    @PostMapping("/category")
    public HttpEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        return adminServiceI.addCategory(categoryDto);
    }

    @PutMapping("/category/{id}")
    public HttpEntity<?> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable int id) {
        return adminServiceI.updateCategory(categoryDto, id);
    }

    @PostMapping("/product")
    public HttpEntity<?> addProduct(@RequestBody ProductDto productDTO) {
        return adminServiceI.addProduct(productDTO);
    }

    @PutMapping("/product/{productId}")
    public HttpEntity<?> updateProduct(@PathVariable Integer productId,@RequestBody ProductDto productDTO) {
        return adminServiceI.updateProduct(productDTO,productId);
    }

    @DeleteMapping("/product/{id}")
    public HttpEntity<?> deleteProduct(@PathVariable int id) {
        return adminServiceI.deleteProduct(id);
    }
    @GetMapping("/delivers")
    public HttpEntity<?> getDeliverers() {
        return adminServiceI.getDeliverers();
    }


    @GetMapping("/operators")
    public HttpEntity<?> getOperators() {
        return adminServiceI.getOperators();
    }

    @PostMapping("create-employee")
    public HttpEntity<?> createDelivererVsOperator(@RequestBody RegisterDto registerDto) {
        return adminServiceI.createDelivererVsOperator(registerDto);
    }


    ///////////////////////////////////////////////////
    @PutMapping("/operator/{id}/update-info")
    public ResponseEntity<?> updateOperatorInfo(@PathVariable Integer id, @Valid @RequestBody OperatorUpdateRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                    request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                    request.getPhone() == null || request.getPhone().trim().isEmpty()) {

                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Username, email, and phone are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }


            Operator operator = operatorService.findById(id);
            if (operator == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Operator not found with ID: " + id);
                return ResponseEntity.notFound().build();
            }

            // Update operator information
            operator.setUsername(request.getUsername());
            operator.setEmail(request.getEmail());
            operator.setPhone(request.getPhone());

            // Save updated operator
            Operator updatedOperator = operatorService.save(operator);

            // Return success response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Operator information updated successfully");
            response.put("operator", updatedOperator);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update operator information: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/delivery/{id}/update-info")
    public ResponseEntity<?> updateDeliverInfo(@PathVariable Integer id, @Valid @RequestBody DeliverUpdateRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                    request.getEmail() == null || request.getEmail().trim().isEmpty() ||
                    request.getPhone() == null || request.getPhone().trim().isEmpty()) {

                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Username, email, and phone are required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            Delivery delivery = delivereyServis.findById(id);
            if (delivery == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Operator not found with ID: " + id);
                return ResponseEntity.notFound().build();
            }

            // Для отладки
            System.out.println("Updating delivery with ID: " + id);
            System.out.println("Username: " + request.getUsername());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Phone: " + request.getPhone());
            System.out.println("Vehicle Number: " + request.getVehicleNumber());
            System.out.println("Online Status: " + request.getOnline());

            delivery.setUsername(request.getUsername());
            delivery.setEmail(request.getEmail());
            delivery.setPhone(request.getPhone());
            delivery.setVehicleNumber(request.getVehicleNumber());

            // Проверяем, что метод существует и работает правильно
            Boolean onlineStatus = request.getOnline();
            if (onlineStatus != null) {
                delivery.setIsOnline(onlineStatus);
                // Альтернативный вариант, если метод называется по-другому
                // delivery.setActive(onlineStatus);
                // delivery.setIsActive(onlineStatus);
            }

            // Сохраняем обновленные данные
            Delivery updatedOperator = delivereyServis.save(delivery);

            // Проверяем, что данные сохранились
            System.out.println("Updated delivery: " + updatedOperator.getId());
            System.out.println("Updated online status: " + updatedOperator.getIsOnline());

            // Возвращаем успешный ответ
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Operator information updated successfully");
            response.put("operator", updatedOperator);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Подробный лог ошибки
            System.err.println("Error updating delivery: " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update operator information: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
/// ///////////////////////////////////////////////////

    @GetMapping("/deliverer{id}")
    public HttpEntity<?> getDeliverer(@PathVariable int id) {
        return adminServiceI.getOperator(id);
    }

    @GetMapping("/operator{id}")
    public HttpEntity<?> getOperator(@PathVariable int id) {
        return adminServiceI.getDeliverer(id);
    }

    @GetMapping("/deliverers/available")
    public HttpEntity<?> getAvailableDeliverer() {
        List<Delivery> all = deliveryRepository.findAll();
        List<Delivery> availableDeliverers = all.stream()
                .filter(item -> item.getIsOnline() && item.getDelivererStatus()
                        .equals(DelivererStatus.AVAILABLE)).toList();

        return ResponseEntity.ok(availableDeliverers);
    }

    @GetMapping("/orders/unassigned")
    public HttpEntity<?> getUnassignedOrders() {
        List<Order> byIsAttached = orderRepository.findByIsAttached(false);
        return ResponseEntity.ok(byIsAttached);
    }

    @PostMapping("/route/save")
    public HttpEntity<?> saveRoute(@RequestBody RouteDto routeDto) {
        Delivery delivery = deliveryRepository.findById(routeDto.getDeliveryId()).orElseThrow();
        Route route = new Route();
        route.setDelivery(delivery);
        delivery.setDelivererStatus(DelivererStatus.ON_DELIVERY);
        deliveryRepository.save(delivery);
        routeRepository.save(route);

        int stepOrder = 1;
        for (Integer orderId : routeDto.getOrderIds()) {
            Order order = orderRepository.findById(orderId).orElseThrow();
            RouteItem routeItem = new RouteItem();
            routeItem.setOrder(order);
            routeItem.setRoute(route);
            routeItem.setStepOrder(stepOrder++);

            order.setIsAttached(true);
            order.setStatus(OrderStatus.IN_PROGRESS);
            order.setDelivery(delivery);
            orderRepository.save(order);
            routeItemRepository.save(routeItem);
        }
        return ResponseEntity.ok("Route saved successfully!");
    }


    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate"));

        OrderStatus orderStatus = status != null ? OrderStatus.valueOf(status) : null;

        Page<OrderResponse> orders = orderService.findOrders(orderStatus, search, pageable);

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/detach")
    public ResponseEntity<OrderResponse> detachOrder(@PathVariable Integer id) {
        OrderResponse detachedOrder = orderService.detachOrder(id);
        return ResponseEntity.ok(detachedOrder);
    }
}

//security, status delivery string,
