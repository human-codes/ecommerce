package uz.pdp.salemartpro.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Order;
import uz.pdp.salemartpro.entity.Route;
import uz.pdp.salemartpro.entity.RouteItem;

import java.util.List;
import java.util.Optional;

public interface RouteItemRepository extends JpaRepository<RouteItem, Integer> {
    Optional<RouteItem> findByOrder(Order order);
    int countByRoute(Route route);
}