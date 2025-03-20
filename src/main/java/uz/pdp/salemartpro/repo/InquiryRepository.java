package uz.pdp.salemartpro.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Inquiry;

public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
}