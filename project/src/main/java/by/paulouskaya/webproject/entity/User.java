package by.paulouskaya.webproject.entity;

public class User extends BeautySalonEntity {
    private int id;
    private String userName;
    private String email;
    private String hashedPassword;
    private String role;

    public User(int id, String userName, String email, String hashedPassword, String role) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
