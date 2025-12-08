package by.paulouskaya.webproject.service.impl;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.dao.impl.UserDaoImpl;
import by.paulouskaya.webproject.model.UserRole;
import by.paulouskaya.webproject.service.RegistrationService;
import by.paulouskaya.webproject.util.PasswordHasher;
import by.paulouskaya.webproject.util.PasswordValidator;
import java.util.List;

public class RegistrationServiceImpl implements RegistrationService {
    private final UserDaoImpl userDaoImpl;

    public RegistrationServiceImpl() {
        this.userDaoImpl = new UserDaoImpl();
    }

    public UserModel register(String username, String email, String password, UserRole role)
            throws ServiceException {

        if (userDaoImpl.existsByUsername(username)) {
            throw new ServiceException("Username already exists: " + username);
        }

        if (userDaoImpl.existsByEmail(email)) {
            throw new ServiceException("Email already registered: " + email);
        }

        List<String> errors = PasswordValidator.validatePassword(password);
        if (!errors.isEmpty()) {
            throw new ServiceException("Invalid password: " + String.join(", ", errors));
        }

        String passwordHash = PasswordHasher.hashPassword(password);

        UserModel user = new UserModel(0, username, email, passwordHash, role);

        return userDaoImpl.save(user);
    }
}