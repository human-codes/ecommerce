package uz.pdp.salemartpro.controller;


import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.salemartpro.service.AttachmentServiceI;

@RestController
@RequestMapping("/api/file")
@MultipartConfig
public class AttachmentController {

    private final AttachmentServiceI attachmentServiceI;

    public AttachmentController(AttachmentServiceI attachmentServiceI) {
        this.attachmentServiceI = attachmentServiceI;
    }

    @GetMapping("/{attachmentId}")
    public HttpEntity<?> getFile(@PathVariable Integer attachmentId, HttpServletResponse response) {
        return attachmentServiceI.getFile(attachmentId, response);
    }

    @PostMapping
    public HttpEntity<?> uploadFile(@RequestParam MultipartFile file) {
        return attachmentServiceI.uploadFile(file);
    }
}


