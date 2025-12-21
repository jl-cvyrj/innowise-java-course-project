package by.paulouskaya.webproject.model;

import by.paulouskaya.webproject.util.UserIdGenerator;
import jakarta.jws.soap.SOAPBinding;

public class UserModel {
    private static final String STRING_BUFFER_START = "UserModel{";
    private static final String HASHED_PASSWORD_FIELD = "hashedPassword='";
    private static final String ROLE_FIELD = ", role='";
    private static final String EMAIL_FIELD = ", email='";
    private static final String ID_FIELD = ", id=";
    private static final String USER_NAME_FIELD = ", userName='";
    private static final char SINGLE_QUOTE = '\'';
    private static final char CLOSING_BRACE = '}';

    private long userIdGenerated = UserIdGenerator.generateUserId();
    private long userId;
    private String userName;
    private String email;
    private String hashedPassword;
    private UserRole role;

    public UserModel(String userName, String email, String hashedPassword, UserRole role) {
        this.userId = userIdGenerated;
        this.userName = userName;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public long getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer(STRING_BUFFER_START);
        stringBuffer.append(HASHED_PASSWORD_FIELD).append(hashedPassword).append(SINGLE_QUOTE);
        stringBuffer.append(ROLE_FIELD).append(role).append(SINGLE_QUOTE);
        stringBuffer.append(EMAIL_FIELD).append(email).append(SINGLE_QUOTE);
        stringBuffer.append(ID_FIELD).append(userId);
        stringBuffer.append(USER_NAME_FIELD).append(userName).append(SINGLE_QUOTE);
        stringBuffer.append(CLOSING_BRACE);
        return stringBuffer.toString();
    }
}
