package by.paulouskaya.webproject.service;

import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.model.PetType;
import by.paulouskaya.webproject.model.ServiceType;
import by.paulouskaya.webproject.exception.ServiceException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingModel createBooking(Long bookingId, Long userId, PetType petType, List<ServiceType> services,
                               LocalDateTime preferredDate, String notes) throws ServiceException;
    List<BookingModel> getUserBookings(Long userId) throws ServiceException;
    BookingModel getBookingById(Long bookingId) throws ServiceException;
    boolean cancelBooking(Long bookingId, Long userId) throws ServiceException;
    List<BookingModel> getAllBookings() throws ServiceException;
    boolean confirmBooking(Long bookingId) throws ServiceException;
    boolean scheduleBooking(Long bookingId, LocalDateTime scheduledDate) throws ServiceException;
    boolean rejectBooking(Long bookingId) throws ServiceException;
    boolean completeBooking(Long bookingId) throws ServiceException;
}