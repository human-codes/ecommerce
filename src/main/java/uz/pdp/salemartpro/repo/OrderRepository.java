package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserUsername(String username);
}