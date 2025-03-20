package uz.pdp.salemartpro.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
}