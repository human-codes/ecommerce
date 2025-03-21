package uz.pdp.salemartpro.dto;

import lombok.Value;

import java.util.List;

@Value
public class RouteDto {
    Integer deliveryId;
    List<Integer> orderIds;
}
