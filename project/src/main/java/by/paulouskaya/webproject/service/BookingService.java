package by.paulouskaya.webproject.service;

import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.model.PetType;
import by.paulouskaya.webproject.model.ServiceType;
import by.paulouskaya.webproject.exception.ServiceException;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingModel createBooking(Long userId, PetType petType, List<ServiceType> services,
                               LocalDateTime preferredDate, String notes) throws ServiceException;
    List<BookingModel> findUserBookings(Long userId) throws ServiceException;
    BookingModel findBookingById(Long bookingId) throws ServiceException;
    boolean cancelBooking(Long bookingId, Long userId) throws ServiceException;
    List<BookingModel> findAllBookings() throws ServiceException;
    boolean confirmBooking(Long bookingId) throws ServiceException;
    boolean scheduleBooking(Long bookingId, LocalDateTime scheduledDate) throws ServiceException;
    boolean rejectBooking(Long bookingId) throws ServiceException;
    boolean completeBooking(Long bookingId) throws ServiceException;
    boolean assignDateTime(Long bookingId, LocalDateTime dateTime) throws ServiceException;

}