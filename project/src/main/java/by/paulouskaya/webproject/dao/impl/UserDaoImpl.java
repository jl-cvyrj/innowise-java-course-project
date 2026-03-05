package by.paulouskaya.webproject.dao.impl;

import by.paulouskaya.webproject.dao.UserDao;
import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.model.UserModel;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class.getName());

    private static final Map<Long, UserModel> userMap = new HashMap<>();
    private static final AtomicLong nextId = new AtomicLong(1);

    public UserModel findByUserName(String userName) throws DaoException {

        logger.info("Searching user by: {}", userName);
        for (UserModel user : userMap.values()) {
            if (user.getUserName() != null &&
                    user.getUserName().equalsIgnoreCase(userName)) {
                return user;
            }
        }
        throw new DaoException("User not found: " + userName);
    }

    public UserModel findById(Long id) throws DaoException {
        UserModel user = userMap.get(id);
        if (user == null) {
            throw new DaoException("User not found with ID: " + id);
        }
        return user;
    }

    public boolean existsByUsername(String username) {
        for (UserModel user : userMap.values()) {
            if (user.getUserName() != null &&
                    user.getUserName().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        for (UserModel user : userMap.values()) {
            if (user.getEmail() != null &&
                    user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public UserModel save(UserModel user) {
        if (user.getUserId() == null) {
            Long newId = nextId.getAndIncrement();
            user.setUserId(newId);
            userMap.put(newId, user);
            logger.info("User created: {}", user.getUserId());
        } else {
            userMap.put(user.getUserId(), user);
            logger.info("User updated: {}", user.getUserId());
        }
        return user;
    }

    public boolean updatePassword(Long userId, String newPasswordHash) throws DaoException {
        UserModel user = findById(userId);
        if (user != null) {
            user.setHashedPassword(newPasswordHash);
            userMap.put(userId, user);
            logger.info("Password updated for user: {}", userId);
            return true;
        }
        return false;
    }

    public List<UserModel> findAll() {
        return new ArrayList<>(userMap.values());
    }

    public boolean delete(Long id) {
        UserModel removed = userMap.remove(id);
        if (removed != null) {
            logger.info("User deleted: {}", removed.getUserId());
            return true;
        }
        return false;
    }
}