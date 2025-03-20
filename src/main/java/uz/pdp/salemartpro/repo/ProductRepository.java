package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategoryId(Integer id);
}