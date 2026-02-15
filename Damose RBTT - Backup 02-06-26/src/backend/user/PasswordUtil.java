package backend.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class PasswordUtil -> a class that allows you to encrypt a password.
 */
public class PasswordUtil {

    /**
     * Hash.
     *
     * @param input the input
     * @return the string
     */
    // SHA-256
    public static String hash(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Password nulla non consentita");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di hashing non disponibile", e);
        }
    }

    /**
     * Bytes to hex.
     *
     * @param bytes the bytes
     * @return the string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
