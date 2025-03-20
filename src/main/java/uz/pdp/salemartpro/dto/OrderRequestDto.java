package uz.pdp.salemartpro.dto;

import lombok.Value;

import java.util.Map;

@Value
public class OrderRequestDto {
     Map<Integer, Integer> basket;
     AddressDto address;
     String phoneNumber;
     String notes;
}
