package by.paulouskaya.webproject.util;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
    private static final int MIN_LENGTH = 8;

    public static List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("Password cannot be empty");
            return errors;
        }

        if (password.length() < MIN_LENGTH) {
            errors.add("Password must be at least " + MIN_LENGTH + " characters");
        }

        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one number");
        }

        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }

        return errors;
    }

    public static boolean isValid(String password) {
        return validatePassword(password).isEmpty();
    }
}