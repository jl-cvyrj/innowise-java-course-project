package by.paulouskaya.webproject.dao.impl;

import by.paulouskaya.webproject.dao.BookingDao;
import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class BookingDaoImpl implements BookingDao {
    private static final Logger logger = LogManager.getLogger(BookingDaoImpl.class);

    private static final Map<Long, BookingModel> bookingMap = new HashMap<>();
    private static long nextId = 1L;

    public BookingModel save(BookingModel booking) throws DaoException {
        try {
            if (booking.getBookingId() == null || booking.getBookingId() == 0) {

                Long newId = nextId++;
                BookingModel newBooking = new BookingModel(
                        newId,
                        booking.getUserId(),
                        booking.getPetType(),
                        booking.getServices(),
                        booking.getPreferredDate(),
                        booking.getStatus(),
                        booking.getNotes()
                );
                bookingMap.put(newId, newBooking);
                logger.info("Booking created: ID={}, User={}", newId, booking.getUserId());
                return newBooking;
            } else {
                bookingMap.put(booking.getBookingId(), booking);
                logger.info("Booking updated: ID={}", booking.getBookingId());
                return booking;
            }
        } catch (Exception e) {
            logger.error("Failed to save booking", e);
            throw new DaoException("Failed to save booking", e);
        }
    }

    public BookingModel findById(Long id) throws DaoException {
        try {
            BookingModel booking = bookingMap.get(id);
            if (booking == null) {
                logger.warn("Booking not found: ID={}", id);
                throw new DaoException("Booking not found with ID: " + id);
            }
            return booking;
        } catch (Exception e) {
            logger.error("Error finding booking by ID: {}", id, e);
            throw new DaoException("Error finding booking by ID: " + id, e);
        }
    }

    public List<BookingModel> findByUserId(Long userId) throws DaoException {
        try {
            List<BookingModel> userBookings = bookingMap.values().stream()
                    .filter(booking -> booking.getUserId().equals(userId))
                    .sorted(Comparator.comparing(BookingModel::getPreferredDate).reversed())
                    .collect(Collectors.toList());

            logger.info("Found {} bookings for user ID: {}", userBookings.size(), userId);
            return userBookings;
        } catch (Exception e) {
            logger.error("Error finding bookings for user ID: {}", userId, e);
            throw new DaoException("Error finding bookings for user ID: " + userId, e);
        }
    }

    public List<BookingModel> findAll() throws DaoException {
        try {
            List<BookingModel> allBookings = new ArrayList<>(bookingMap.values());
            allBookings.sort(Comparator.comparing(BookingModel::getPreferredDate).reversed());

            logger.info("Found {} total bookings", allBookings.size());
            return allBookings;
        } catch (Exception e) {
            logger.error("Error finding all bookings", e);
            throw new DaoException("Failed to retrieve all bookings", e);
        }
    }

    public boolean update(BookingModel booking) throws DaoException {
        try {
            if (!bookingMap.containsKey(booking.getBookingId())) {
                logger.warn("Cannot update non-existent booking: ID={}", booking.getBookingId());
                throw new DaoException("Booking not found: " + booking.getBookingId());
            }

            bookingMap.put(booking.getBookingId(), booking);
            logger.info("Booking updated successfully: ID={}", booking.getBookingId());
            return true;
        } catch (Exception e) {
            logger.error("Failed to update booking ID: {}", booking.getBookingId(), e);
            throw new DaoException("Failed to update booking", e);
        }
    }

    public boolean delete(Long id) throws DaoException {
        try {
            BookingModel removed = bookingMap.remove(id);
            if (removed != null) {
                logger.info("Booking deleted: ID={}", id);
                return true;
            }
            logger.warn("Booking not found for deletion: ID={}", id);
            return false;
        } catch (Exception e) {
            logger.error("Failed to delete booking ID: {}", id, e);
            throw new DaoException("Failed to delete booking", e);
        }
    }

    public List<BookingModel> findByStatus(String status) throws DaoException {
        try {
            return bookingMap.values().stream()
                    .filter(booking -> booking.getStatus().name().equals(status))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DaoException("Failed to find bookings by status: " + status, e);
        }
    }
}