package uz.pdp.salemartpro.email;
import org.springframework.stereotype.Component;



@Component
public class EmailContentBuilder {

    public String getVerificationEmailContent(String code) {
        return "<p>Your verification code is: <strong>" + code + "</strong></p>";
    }

    public String getPasswordResetEmailContent(String code) {
        return "<p>Your password reset code is: <strong>" + code + "</strong></p>";
    }
}

