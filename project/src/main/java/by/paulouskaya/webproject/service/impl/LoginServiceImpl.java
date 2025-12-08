package by.paulouskaya.webproject.service.impl;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.dao.impl.UserDaoImpl;
import by.paulouskaya.webproject.service.LoginService;
import by.paulouskaya.webproject.util.PasswordHasher;

public class LoginServiceImpl implements LoginService {
    private final UserDaoImpl userDaoImpl;

    public LoginServiceImpl() {
        this.userDaoImpl = new UserDaoImpl();
    }

    public UserModel authenticate(String username, String password) throws ServiceException {

        try {
            UserModel user = userDaoImpl.findByUserName(username);
            String storedHash = user.getHashedPassword();
            if (!PasswordHasher.checkPassword(password, storedHash)) {
                throw new ServiceException("Invalid password for user: " + username);
            }
            return user;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}