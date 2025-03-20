package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}