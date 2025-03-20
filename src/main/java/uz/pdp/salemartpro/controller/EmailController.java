//package uz.pdp.salemartpro.controller;
//
//import jakarta.mail.MessagingException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import uz.pdp.salemartpro.service.EmailService;
//
//@RestController
//@RequestMapping("/api/email")
//public class EmailController {
//
//    private final EmailService emailService;
//
//    public EmailController(EmailService emailService) {
//        this.emailService = emailService;
//    }
//
//    @PostMapping("/send")
//    public String sendEmail(@RequestParam String email) {
//        try {
//            String code = emailService.sendVerificationCode(email);
//            return "Verification code sent to " + email + ": " + code;
//        } catch (MessagingException e) {
//            return "Failed to send email.";
//        }
//    }
//}
