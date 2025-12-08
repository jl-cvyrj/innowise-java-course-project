package by.paulouskaya.webproject.service;

import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;

public interface LoginService {
    UserModel authenticate(String username, String password) throws ServiceException;
}
