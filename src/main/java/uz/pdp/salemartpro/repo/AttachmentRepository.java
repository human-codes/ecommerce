package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
}