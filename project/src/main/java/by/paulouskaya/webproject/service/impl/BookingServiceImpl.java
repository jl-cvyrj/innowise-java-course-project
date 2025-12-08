package by.paulouskaya.webproject.service.impl;

import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.model.PetType;
import by.paulouskaya.webproject.model.ServiceType;
import by.paulouskaya.webproject.model.BookingStatus;
import by.paulouskaya.webproject.dao.impl.BookingDaoImpl;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.service.BookingService;
import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingDaoImpl bookingDaoImpl;

    public BookingServiceImpl() {
        this.bookingDaoImpl = new BookingDaoImpl();
    }

    @Override
    public BookingModel createBooking(Long userId, PetType petType, List<ServiceType> services,
                                      LocalDateTime preferredDate, String notes) throws ServiceException {
        try {
            BookingModel booking = new BookingModel(
                    null,
                    userId,
                    petType,
                    services,
                    preferredDate,
                    BookingStatus.PENDING,
                    notes
            );

            return bookingDaoImpl.save(booking);

        } catch (Exception e) {
            throw new ServiceException("Failed to create booking", e);
        }
    }

    @Override
    public List<BookingModel> getUserBookings(Long userId) throws ServiceException {
        try {
            return bookingDaoImpl.findByUserId(userId);
        } catch (Exception e) {
            throw new ServiceException("Failed to get user bookings", e);
        }
    }

    @Override
    public BookingModel getBookingById(Long bookingId) throws ServiceException {
        try {
            BookingModel booking = bookingDaoImpl.findById(bookingId);
            if (booking == null) {
                throw new ServiceException("Booking not found: " + bookingId);
            }
            return booking;
        } catch (Exception e) {
            throw new ServiceException("Failed to get booking", e);
        }
    }

    @Override
    public boolean cancelBooking(Long bookingId, Long userId) throws ServiceException {
        try {
            BookingModel booking = bookingDaoImpl.findById(bookingId);

            if (booking == null || !booking.getUserId().equals(userId)) {
                return false;
            }

            if (!booking.getStatus().canBeCancelled()) {
                return false;
            }

            booking.setStatus(BookingStatus.CANCELLED);
            bookingDaoImpl.update(booking);
            return true;

        } catch (Exception e) {
            throw new ServiceException("Failed to cancel booking", e);
        }
    }

    @Override
    public List<BookingModel> getAllBookings() throws ServiceException {
        try {
            return bookingDaoImpl.findAll();
        } catch (Exception e) {
            throw new ServiceException("Failed to get all bookings", e);
        }
    }

    @Override
    public boolean confirmBooking(Long bookingId) throws ServiceException {
        try {
            BookingModel booking = bookingDaoImpl.findById(bookingId);
            if (booking == null) {
                return false;
            }

            if (booking.getStatus() != BookingStatus.PENDING) {
                return false;
            }

            booking.setStatus(BookingStatus.CONFIRMED);
            bookingDaoImpl.update(booking);
            return true;

        } catch (Exception e) {
            throw new ServiceException("Failed to confirm booking", e);
        }
    }

    @Override
    public boolean scheduleBooking(Long bookingId, LocalDateTime scheduledDate) throws ServiceException {
        try {
            BookingModel booking = bookingDaoImpl.findById(bookingId);
            if (booking == null) {
                return false;
            }

            bookingDaoImpl.update(booking);
            return true;

        } catch (Exception e) {
            throw new ServiceException("Failed to schedule booking", e);
        }
    }

    @Override
    public boolean rejectBooking(Long bookingId) throws ServiceException {
        try {
            BookingModel booking = bookingDaoImpl.findById(bookingId);
            if (booking == null) {
                return false;
            }

            if (booking.getStatus() != BookingStatus.PENDING) {
                return false;
            }

            booking.setStatus(BookingStatus.REJECTED);
            bookingDaoImpl.update(booking);
            return true;

        } catch (Exception e) {
            throw new ServiceException("Failed to reject booking", e);
        }
    }

    @Override
    public boolean completeBooking(Long bookingId) throws ServiceException {
        try {
            BookingModel booking = bookingDaoImpl.findById(bookingId);
            if (booking == null) {
                return false;
            }

            if (booking.getStatus() != BookingStatus.CONFIRMED) {
                return false;
            }

            booking.setStatus(BookingStatus.COMPLETED);
            bookingDaoImpl.update(booking);
            return true;

        } catch (Exception e) {
            throw new ServiceException("Failed to complete booking", e);
        }
    }
}