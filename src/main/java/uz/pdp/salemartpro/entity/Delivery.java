package uz.pdp.salemartpro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
public class Delivery extends BaseUser {
    private String vehicleNumber;

    @Column(unique = true)
    private Long telegramId;

}
