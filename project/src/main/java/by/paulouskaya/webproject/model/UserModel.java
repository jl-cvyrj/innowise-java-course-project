package by.paulouskaya.webproject.model;

public class UserModel {
    private long id;
    private String userName;
    private String email;
    private String hashedPassword;
    private String role;

    public UserModel(long id, String userName, String email, String hashedPassword, String role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public long getUserId() { return id; }
    public void setUserId(int id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
