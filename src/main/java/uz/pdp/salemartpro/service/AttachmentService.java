package uz.pdp.salemartpro.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.salemartpro.entity.Attachment;
import uz.pdp.salemartpro.entity.AttachmentContent;
import uz.pdp.salemartpro.repo.AttachmentContentRepository;
import uz.pdp.salemartpro.repo.AttachmentRepository;

import java.io.IOException;

@Service
public class AttachmentService implements AttachmentServiceI {

    private final AttachmentContentRepository attachmentContentRepository;
    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentContentRepository attachmentContentRepository, AttachmentRepository attachmentRepository) {
        this.attachmentContentRepository = attachmentContentRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public HttpEntity<?> getFile(Integer attachmentId, HttpServletResponse response) {
        // Retrieve the attachment by id
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Retrieve the attachment content
        AttachmentContent content = attachmentContentRepository.findByAttachmentId(attachmentId);
        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        // Return file bytes with a proper Content-Type and inline Content-Disposition header
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Adjust this if your file is PNG or another type
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + attachment.getFileName() + "\"")
                .body(content.getContent());
    }

    @Override
    public HttpEntity<?> uploadFile(MultipartFile file) {
        // Create and save the attachment record
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachmentRepository.save(attachment);

        // Create and save the attachment content
        AttachmentContent attachmentContent = new AttachmentContent();
        try {
            attachmentContent.setContent(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file bytes", e);
        }
        attachmentContent.setAttachment(attachment);
        attachmentContentRepository.save(attachmentContent);

        // Return the file id with HTTP status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(attachment.getId());
    }
}
