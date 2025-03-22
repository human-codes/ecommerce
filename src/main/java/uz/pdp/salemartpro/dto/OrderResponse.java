package uz.pdp.salemartpro.dto;

import lombok.Data;
import uz.pdp.salemartpro.entity.enums.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
     private Integer id;
     private LocalDateTime orderDate;
     private OrderStatus status;
     private Boolean isAttached;
     private String phoneNumber;
     private String notes;
     private UserDto user;
     private AddressDto deliveredAddress;
     private DeliveryResponse delivery;

     // Add any other fields needed by your frontend
}