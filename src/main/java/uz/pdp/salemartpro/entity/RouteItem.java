package uz.pdp.salemartpro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RouteItem extends BaseEntity {

    @Column(nullable = false)
    private Integer stepOrder;

    @ManyToOne
    private Route route;

    @ManyToOne
    private Order order;
}
