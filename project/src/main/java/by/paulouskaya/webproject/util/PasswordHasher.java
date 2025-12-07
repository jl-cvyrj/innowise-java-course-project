package by.paulouskaya.webproject.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final String DELIMITER = ":";
    private static final int ITERATIONS = 100000;

    public static String hashPassword(String password) {
        try {
            byte[] salt = generateSalt();
            byte[] hash = hashWithSalt(password, salt, ITERATIONS);

            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);

            return ITERATIONS + DELIMITER + saltBase64 + DELIMITER + hashBase64;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm error", e);
        }
    }

    public static boolean checkPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }

        try {
            String[] parts = storedHash.split(DELIMITER);
            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

            byte[] actualHash = hashWithSalt(password, salt, iterations);

            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private static byte[] hashWithSalt(String password, byte[] salt, int iterations)
            throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);

        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        for (int i = 1; i < iterations; i++) {
            digest.reset();
            hash = digest.digest(hash);
        }

        return hash;
    }

    public static boolean isHashedPassword(String password) {
        if (password == null) {
            return false;
        }

        String[] parts = password.split(DELIMITER);
        if (parts.length != 3) {
            return false;
        }

        try {
            Integer.parseInt(parts[0]);
            Base64.getDecoder().decode(parts[1]);
            Base64.getDecoder().decode(parts[2]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}