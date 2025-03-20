package uz.pdp.salemartpro.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.RouteItem;

public interface RouteItemRepository extends JpaRepository<RouteItem, Integer> {
}