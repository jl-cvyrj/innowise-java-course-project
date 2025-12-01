package by.paulouskaya.webproject.dao;

import by.paulouskaya.webproject.entity.User;

import java.util.List;
import java.util.Objects;

public class UserDao extends AbstractDao<Integer, User> {
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

    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return userName; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public void setRole(String role) { this.role = role; }
    public String getRole() { return role; }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User findEntityById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(User entity) {
        return false;
    }

    @Override
    public boolean create(User entity) {
        return false;
    }

    @Override
    public User update(User entity) {
        return null;
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
}
