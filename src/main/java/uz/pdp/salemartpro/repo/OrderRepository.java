package uz.pdp.salemartpro.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uz.pdp.salemartpro.entity.Order;
import uz.pdp.salemartpro.entity.enums.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    List<Order> findByUserUsername(String username);
    List<Order> findByIsAttached(Boolean isAttached);

    // for orders pages
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByStatusAndUserUsernameContainingIgnoreCaseOrStatusAndPhoneNumberContainingIgnoreCase(
            OrderStatus status1, String username, OrderStatus status2, String phoneNumber, Pageable pageable);
}