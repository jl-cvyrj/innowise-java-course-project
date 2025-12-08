package by.paulouskaya.webproject.service;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.model.UserRole;

public interface RegistrationService {
    UserModel register(String username, String email, String password, UserRole role)
            throws ServiceException;
}
