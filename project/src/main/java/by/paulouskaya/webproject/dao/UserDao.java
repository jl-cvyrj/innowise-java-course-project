package by.paulouskaya.webproject.dao;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.model.UserModel;
import java.util.List;

public interface UserDao {

  public UserModel findByUserName(String userName) throws DaoException;

  public UserModel findById(Long id) throws DaoException;

  public boolean existsByUsername(String username);

  public boolean existsByEmail(String email);

  public UserModel save(UserModel user) throws DaoException;

  public boolean updatePassword(Long userId, String newPasswordHash) throws DaoException;

  public List<UserModel> findAll() throws DaoException;

  public boolean delete(Long id) throws DaoException;
}