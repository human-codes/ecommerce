package uz.pdp.salemartpro.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentServiceI {
    HttpEntity<?> getFile(Integer attachmentId, HttpServletResponse response);

    HttpEntity<?> uploadFile(MultipartFile file);
}
