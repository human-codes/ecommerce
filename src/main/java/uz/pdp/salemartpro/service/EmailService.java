package uz.pdp.salemartpro.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.pdp.salemartpro.email.EmailContentBuilder;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailContentBuilder emailContentBuilder;

    public EmailService(JavaMailSender mailSender, EmailContentBuilder emailContentBuilder) {
        this.mailSender = mailSender;
        this.emailContentBuilder = emailContentBuilder;
    }

    public void sendVerificationEmail(String to, String code) {
        String content = emailContentBuilder.getVerificationEmailContent(code);
        sendEmail(to, "Your Verification Code", content);
    }

    public void sendResetPasswordEmail(String to, String code) {
        String content = emailContentBuilder.getPasswordResetEmailContent(code);
        sendEmail(to, "Your Password Reset Code", content);
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
