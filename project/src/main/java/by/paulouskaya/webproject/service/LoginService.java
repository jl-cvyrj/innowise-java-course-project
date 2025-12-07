package by.paulouskaya.webproject.service;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.dao.UserDao;
import by.paulouskaya.webproject.util.PasswordHasher;

import java.rmi.server.ServerCloneException;

public class LoginService {
    private final UserDao userDao;

    public LoginService() {
        this.userDao = new UserDao();
    }

    public UserModel authenticate(String username, String password) throws ServiceException {

        try {
            UserModel user = userDao.findByUserName(username);
            String storedHash = user.getHashedPassword();
            return user;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}