package by.paulouskaya.webproject.dao;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.model.UserModel;
import java.util.List;

public interface UserDao {
    UserModel findByUserName(String userName) throws DaoException;
    UserModel findById(Long id) throws DaoException;
    boolean existsByUsername(String username) throws DaoException;
    boolean existsByEmail(String email) throws DaoException;
    UserModel save(UserModel user) throws DaoException;
    boolean updatePassword(Long userId, String newPasswordHash) throws DaoException;
    List<UserModel> findAll() throws DaoException;
    UserModel findEntityById(Integer id) throws DaoException;
    boolean delete(Integer id) throws DaoException;
    boolean create(UserModel entity) throws DaoException;
    UserModel update(UserModel entity) throws DaoException;
}