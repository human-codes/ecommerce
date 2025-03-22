package uz.pdp.salemartpro.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.salemartpro.dto.*;
import uz.pdp.salemartpro.entity.Delivery;
import uz.pdp.salemartpro.entity.Operator;
import uz.pdp.salemartpro.entity.Order;
import uz.pdp.salemartpro.entity.User;
import uz.pdp.salemartpro.entity.enums.DelivererStatus;
import uz.pdp.salemartpro.entity.enums.RoleName;
import uz.pdp.salemartpro.repo.DeliveryRepository;
import uz.pdp.salemartpro.repo.OrderRepository;
import uz.pdp.salemartpro.repo.UserRepository;
import uz.pdp.salemartpro.service.AdminServiceI;
import uz.pdp.salemartpro.service.DelivereyServis;
import uz.pdp.salemartpro.service.OperatorService;

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
    private final UserRepository userRepository;

    public AdminController(AdminServiceI adminServiceI, DelivereyServis delivereyServis, OperatorService operatorService, DeliveryRepository deliveryRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.adminServiceI = adminServiceI;
        this.delivereyServis = delivereyServis;
        this.operatorService = operatorService;
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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
    public HttpEntity<?> updateProduct(@PathVariable Integer productId, @RequestBody ProductDto productDTO) {
        return adminServiceI.updateProduct(productDTO, productId);
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

            // Update operator information
            delivery.setUsername(request.getUsername());
            delivery.setEmail(request.getEmail());
            delivery.setPhone(request.getPhone());
            delivery.setVehicleNumber(request.getVehicleNumber());

            // Save updated operator
            Delivery updatedOperator = delivereyServis.save(delivery);

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

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}


//security, status delivery string,
