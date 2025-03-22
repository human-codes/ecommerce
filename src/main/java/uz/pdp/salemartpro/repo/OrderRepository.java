package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.salemartpro.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserUsername(String username);

    List<Order> findByIsAttached(Boolean isAttached);

    List<Order> findByUserId(Integer userId);
}