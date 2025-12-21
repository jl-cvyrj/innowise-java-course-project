package by.paulouskaya.webproject.util;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {

  private static final int MIN_LENGTH = 8;
  private static final String NUMBER_REGEX = ".*\\d.*";
  private static final String UPPERCASE_REGEX = ".*[A-Z].*";
  private static final String LOWERCASE_REGEX = ".*[a-z].*";

  public static List<String> validatePassword(String password) {
    List<String> errors = new ArrayList<>();

    if (password == null || password.isEmpty()) {
      errors.add("Password cannot be empty");
      return errors;
    }

    if (password.length() < MIN_LENGTH) {
      errors.add("Password must be at least " + MIN_LENGTH + " characters");
    }

    if (!password.matches(NUMBER_REGEX)) {
      errors.add("Password must contain at least one number");
    }

    if (!password.matches(UPPERCASE_REGEX)) {
      errors.add("Password must contain at least one uppercase letter");
    }

    if (!password.matches(LOWERCASE_REGEX)) {
      errors.add("Password must contain at least one lowercase letter");
    }

    return errors;
  }

  public static boolean isValid(String password) {
    return validatePassword(password).isEmpty();
  }
}
