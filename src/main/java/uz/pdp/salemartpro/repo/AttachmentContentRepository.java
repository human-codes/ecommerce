package uz.pdp.salemartpro.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.salemartpro.entity.AttachmentContent;

public interface AttachmentContentRepository extends JpaRepository<AttachmentContent, Integer> {
    AttachmentContent findByAttachmentId(Integer attachmentId);
}