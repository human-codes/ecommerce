package uz.pdp.salemartpro.email;




import java.security.SecureRandom;

public class CodeGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        int code = 1000 + random.nextInt(9000); // Generates a number between 1000-9999
        System.out.println(code);
        return String.valueOf(code);
    }
}
