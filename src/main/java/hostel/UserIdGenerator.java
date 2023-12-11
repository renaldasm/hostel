package hostel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserIdGenerator {
    public static String generateUserId(String email) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] emailBytes = email.getBytes();
            byte[] digest = md.digest(emailBytes);

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception
            e.printStackTrace();
            return null;
        }
    }
}
