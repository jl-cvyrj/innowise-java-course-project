package by.paulouskaya.webproject.dao.impl;

import by.paulouskaya.webproject.connection.ConnectionPool;
import by.paulouskaya.webproject.dao.BookingDao;
import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.model.PetType;
import by.paulouskaya.webproject.model.ServiceType;
import by.paulouskaya.webproject.model.BookingStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookingDaoJdbcImpl implements BookingDao {

    private static final Logger logger = LogManager.getLogger(BookingDaoJdbcImpl.class);

    private static final String INSERT_SQL =
            "INSERT INTO bookings (user_id, pet_type, services, preferred_date, status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE bookings SET pet_type=?, services=?, preferred_date=?, status=?, notes=? WHERE booking_id=?";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM bookings WHERE booking_id=?";

    private static final String SELECT_BY_USER_SQL =
            "SELECT * FROM bookings WHERE user_id=? ORDER BY preferred_date DESC";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM bookings ORDER BY preferred_date DESC";

    private static final String DELETE_SQL =
            "DELETE FROM bookings WHERE booking_id=?";

    private static final String SELECT_BY_STATUS_SQL =
            "SELECT * FROM bookings WHERE status=? ORDER BY preferred_date DESC";

    @Override
    public BookingModel save(BookingModel booking) throws DaoException {
        if (booking.getBookingId() == null || booking.getBookingId() == 0) {
            // INSERT
            try (Connection conn = ConnectionPool.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setLong(1, booking.getUserId());
                stmt.setString(2, booking.getPetType() != null ? booking.getPetType().name() : null);
                stmt.setString(3, booking.getServicesAsString());
                stmt.setTimestamp(4, booking.getPreferredDate() != null ? Timestamp.valueOf(booking.getPreferredDate()) : null);
                stmt.setString(5, booking.getStatus() != null ? booking.getStatus().name() : null);
                stmt.setString(6, booking.getNotes());

                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    throw new DaoException("Creating booking failed, no rows affected.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        booking.setBookingId(generatedKeys.getLong(1));
                    } else {
                        throw new DaoException("Creating booking failed, no ID obtained.");
                    }
                }

                logger.info("Booking created: {}", booking);
                return booking;

            } catch (SQLException e) {
                logger.error("Error saving booking", e);
                throw new DaoException("Error saving booking", e);
            }
        } else {
            // UPDATE
            try (Connection conn = ConnectionPool.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

                stmt.setString(1, booking.getPetType() != null ? booking.getPetType().name() : null);
                stmt.setString(2, booking.getServicesAsString());
                stmt.setTimestamp(3, booking.getPreferredDate() != null ? Timestamp.valueOf(booking.getPreferredDate()) : null);
                stmt.setString(4, booking.getStatus() != null ? booking.getStatus().name() : null);
                stmt.setString(5, booking.getNotes());
                stmt.setLong(6, booking.getBookingId());

                int affected = stmt.executeUpdate();
                if (affected == 0) {
                    throw new DaoException("Updating booking failed, booking not found.");
                }

                logger.info("Booking updated: {}", booking);
                return booking;

            } catch (SQLException e) {
                logger.error("Error updating booking", e);
                throw new DaoException("Error updating booking", e);
            }
        }
    }

    @Override
    public BookingModel findById(Long id) throws DaoException {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBooking(rs);
                }
            }

            throw new DaoException("Booking not found with ID: " + id);

        } catch (SQLException e) {
            logger.error("Error finding booking by ID", e);
            throw new DaoException("Error finding booking by ID", e);
        }
    }

    @Override
    public List<BookingModel> findByUserId(Long userId) throws DaoException {
        List<BookingModel> list = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_USER_SQL)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToBooking(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            logger.error("Error finding bookings for user ID: {}", userId, e);
            throw new DaoException("Error finding bookings for user ID: " + userId, e);
        }
    }

    @Override
    public List<BookingModel> findAll() throws DaoException {
        List<BookingModel> list = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToBooking(rs));
            }
            return list;

        } catch (SQLException e) {
            logger.error("Error finding all bookings", e);
            throw new DaoException("Error finding all bookings", e);
        }
    }

    @Override
    public boolean update(BookingModel booking) throws DaoException {
        save(booking);
        return true;
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;

        } catch (SQLException e) {
            logger.error("Error deleting booking ID: {}", id, e);
            throw new DaoException("Error deleting booking ID: " + id, e);
        }
    }

    @Override
    public List<BookingModel> findByStatus(String status) throws DaoException {
        List<BookingModel> list = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_STATUS_SQL)) {

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToBooking(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            logger.error("Error finding bookings by status: {}", status, e);
            throw new DaoException("Error finding bookings by status: " + status, e);
        }
    }
    
    private BookingModel mapRowToBooking(ResultSet rs) throws SQLException {
        String servicesText = rs.getString("services");
        List<ServiceType> services = new ArrayList<>();
        if (servicesText != null && !servicesText.isBlank()) {
            services = Arrays.stream(servicesText.split(","))
                    .map(String::trim)
                    .map(ServiceType::fromDisplayName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        PetType petType = null;
        String petTypeStr = rs.getString("pet_type");
        if (petTypeStr != null) petType = PetType.valueOf(petTypeStr);

        BookingStatus status = null;
        String statusStr = rs.getString("status");
        if (statusStr != null) status = BookingStatus.valueOf(statusStr);

        LocalDateTime preferredDate = null;
        Timestamp ts = rs.getTimestamp("preferred_date");
        if (ts != null) preferredDate = ts.toLocalDateTime();

        BookingModel booking = new BookingModel(
                rs.getLong("user_id"),
                petType,
                services,
                preferredDate,
                status,
                rs.getString("notes")
        );
        booking.setBookingId(rs.getLong("booking_id"));
        return booking;
    }
}