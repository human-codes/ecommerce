package uz.pdp.salemartpro.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}