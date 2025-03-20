package uz.pdp.salemartpro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {
    private String fullAddress;
    private Float longitude;
    private Float latitude;
    private Boolean isAdmin=false;
}
