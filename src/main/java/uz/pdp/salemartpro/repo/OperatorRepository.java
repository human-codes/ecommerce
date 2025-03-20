package uz.pdp.salemartpro.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Operator;

public interface OperatorRepository extends JpaRepository<Operator, Integer> {
}