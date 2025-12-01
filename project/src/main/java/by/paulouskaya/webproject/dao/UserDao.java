package by.paulouskaya.webproject.dao;

import java.sql.DriverManager;
import java.util.Objects;

public class UserDao extends AbstractDao {
    private String userName;
    private String email;
    private String hashedPassword;
    private String role;

    public UserDao(String id, String userName, String email, String hashedPassword, String role) {
        super(id);
        this.userName = userName;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDao userDao = (UserDao) o;
        return Objects.equals(userName, userDao.userName) && Objects.equals(email, userDao.email) && Objects.equals(hashedPassword, userDao.hashedPassword) && Objects.equals(role, userDao.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email, hashedPassword, role);
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer("UserDao{");
        stringBuffer.append("userName='").append(userName).append('\'');
        stringBuffer.append(", email='").append(email).append('\'');
        stringBuffer.append(", hashedPassword='").append(hashedPassword).append('\'');
        stringBuffer.append(", role='").append(role).append('\'');
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    @Override
    public void insert(Object entity) {
        UserDao user = (UserDao) entity;
        String sql = "INSERT INTO users (userName, email, hashedPassword, role) VALUES (?, ?, ?, ?)";
//        try (Connection conn = DriverManager.getConnection(connectionUrl, username, password);
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, user.getUserName());
//            stmt.setString(2, user.getEmail());
//            stmt.setString(3, user.getHashedPassword());
//            stmt.setString(4, user.getRole());
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void update(Object entity) {
    }

    @Override
    public void delete(Object entity) {
    }

    @Override
    public Object findById(int id) {
        return null;
    }
}
