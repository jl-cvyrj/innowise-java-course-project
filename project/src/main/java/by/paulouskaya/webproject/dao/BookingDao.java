package by.paulouskaya.webproject.dao;

import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.exception.DaoException;
import java.util.List;

public interface BookingDao {

    public BookingModel save(BookingModel booking) throws DaoException;

    public BookingModel findById(Long id) throws DaoException;

    public List<BookingModel> findByUserId(Long userId) throws DaoException;

    public List<BookingModel> findAll() throws DaoException;

    public boolean update(BookingModel booking) throws DaoException;
}