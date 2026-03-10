package by.paulouskaya.webproject.dao.impl;

import by.paulouskaya.webproject.connection.ConnectionPool;
import by.paulouskaya.webproject.dao.UserDao;
import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDaoJdbcImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoJdbcImpl.class);

    private static final String INSERT_USER_SQL =
            "INSERT INTO users (user_name, email, hashed_password, role) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM users WHERE user_id = ?";
    private static final String SELECT_BY_USERNAME_SQL =
            "SELECT * FROM users WHERE user_name = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT * FROM users";
    private static final String UPDATE_USER_SQL =
            "UPDATE users SET user_name = ?, email = ?, hashed_password = ?, role = ? WHERE user_id = ?";
    private static final String DELETE_USER_SQL =
            "DELETE FROM users WHERE user_id = ?";

    @Override
    public UserModel save(UserModel user) throws DaoException {
        if (user.getUserId() == null) {
            // INSERT
            try (Connection conn = ConnectionPool.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, user.getUserName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getHashedPassword());
                ps.setString(4, user.getRole().name());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setUserId(rs.getLong(1));
                    }
                }

                logger.info("User created: ID={}", user.getUserId());
                return user;

            } catch (SQLException e) {
                logger.error("Failed to save user", e);
                throw new DaoException("Failed to save user", e);
            }
        } else {
            // UPDATE
            try (Connection conn = ConnectionPool.getInstance().getConnection();
                 PreparedStatement ps = conn.prepareStatement(UPDATE_USER_SQL)) {

                ps.setString(1, user.getUserName());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getHashedPassword());
                ps.setString(4, user.getRole().name());
                ps.setLong(5, user.getUserId());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new DaoException("User not found for update: " + user.getUserId());
                }

                logger.info("User updated: ID={}", user.getUserId());
                return user;

            } catch (SQLException e) {
                logger.error("Failed to update user", e);
                throw new DaoException("Failed to update user", e);
            }
        }
    }

    @Override
    public boolean updatePassword(Long userId, String newPasswordHash) throws DaoException {
        String sql = "UPDATE users SET hashed_password = ? WHERE user_id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setLong(2, userId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.error("Error updating password for user ID={}", userId, e);
            throw new DaoException("Error updating password for user ID=" + userId, e);
        }
    }

    @Override
    public UserModel findById(Long id) throws DaoException {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                } else {
                    throw new DaoException("User not found: ID=" + id);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding user by ID", e);
            throw new DaoException("Error finding user by ID", e);
        }
    }

    @Override
    public boolean existsByUsername(String username) throws DaoException {
        String sql = "SELECT COUNT(*) FROM users WHERE user_name = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking username existence", e);
            throw new DaoException("Error checking username existence", e);
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) throws DaoException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking email existence", e);
            throw new DaoException("Error checking email existence", e);
        }
        return false;
    }

    @Override
    public UserModel findByUserName(String username) throws DaoException {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_USERNAME_SQL)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                } else {
                    throw new DaoException("User not found: username=" + username);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding user by username", e);
            throw new DaoException("Error finding user by username", e);
        }
    }

    @Override
    public List<UserModel> findAll() throws DaoException {
        List<UserModel> users = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;

        } catch (SQLException e) {
            logger.error("Error finding all users", e);
            throw new DaoException("Error finding all users", e);
        }
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER_SQL)) {

            ps.setLong(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            logger.error("Error deleting user", e);
            throw new DaoException("Error deleting user", e);
        }
    }

    private UserModel mapRowToUser(ResultSet rs) throws SQLException {
        Long id = rs.getLong("user_id");
        String username = rs.getString("user_name");
        String email = rs.getString("email");
        String password = rs.getString("hashed_password");
        UserRole role = UserRole.valueOf(rs.getString("role"));
        return new UserModel(username, email, password, role) {{ setUserId(id); }};
    }
}