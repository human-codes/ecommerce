package uz.pdp.salemartpro.controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.HttpEntity;
import uz.pdp.salemartpro.dto.OrderRequestDto;
import uz.pdp.salemartpro.service.OrderServiceI;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderServiceI orderServiceI;

    public OrderController(OrderServiceI orderServiceI) {
        this.orderServiceI = orderServiceI;
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
    public HttpEntity<?> receiveBasket(@AuthenticationPrincipal UserDetails userDetails,@RequestBody OrderRequestDto orderRequest) {
        return orderServiceI.receiveBasket(userDetails.getUsername(),orderRequest);
    }



}
