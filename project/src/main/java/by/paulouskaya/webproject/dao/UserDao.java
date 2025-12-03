package by.paulouskaya.webproject.dao;

import by.paulouskaya.webproject.model.UserModel;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDao extends AbstractDao<Integer, UserModel> {
    private static final Logger logger = LogManager.getLogger(UserDao.class.getName());

    private static final Map<Integer, UserModel> userMap = new HashMap<>();
    private static int nextId = 1;

    public UserDao() {
        super("user_id");
    }

    public UserModel findByUserName(String userName) {
        logger.info("Searching user by: " + userName);

        for (UserModel user : userMap.values()) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
                return user;
            }
        }
        return null;
    }

    public boolean existsByUsername(String username) {
        for (UserModel user : userMap.values()) {
            if (user.getUserName().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        for (UserModel user : userMap.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<UserModel> findAll() {
        List<UserModel> userList = new ArrayList<>();
        for (UserModel user : userMap.values()) {
            userList.add(user);
        }
        return userList;
    }

    @Override
    public UserModel findEntityById(Integer id) {
        return userMap.get(id);
    }

    @Override
    public boolean delete(Integer id) {
        UserModel removed = userMap.remove(id);
        if (removed != null) {
            logger.info("User deleted: " + removed.getUserId());
            return true;
        }
        return false;
    }

    @Override
    public boolean create(UserModel entity) {
        return false;
    }

    @Override
    public UserModel update(UserModel entity) {
        return null;
    }
}