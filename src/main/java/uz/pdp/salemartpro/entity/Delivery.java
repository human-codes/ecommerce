package uz.pdp.salemartpro.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.salemartpro.entity.enums.DelivererStatus;


@Entity
@Table(name = "deliveries")
@Getter
@Setter
public class Delivery extends BaseUser {
    private String vehicleNumber;

    @Column(unique = true)
    private Long telegramId;

    private Boolean isOnline=true;

    @Enumerated(EnumType.STRING)
    private DelivererStatus delivererStatus=DelivererStatus.AVAILABLE;
}
