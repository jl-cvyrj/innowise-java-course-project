package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.model.PetType;
import by.paulouskaya.webproject.model.ServiceType;
import by.paulouskaya.webproject.service.BookingService;
import by.paulouskaya.webproject.service.impl.BookingServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "BookingServlet", value = "/booking/*")
public class BookingServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(BookingServlet.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(ServletParameter.DATE_TIME_PATTERN);

    private BookingService bookingService;

    @Override
    public void init() {
        bookingService = new BookingServiceImpl();
        logger.info("BookingServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        logger.info("GET /bookings request from IP: {}", clientIp);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(ServletParameter.USER_ATTRIBUTE) == null) {
            logger.warn("Unauthorized access attempt to /bookings from IP: {}", clientIp);
            response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_LOGIN_PATH);
            return;
        }

        String username = (String) session.getAttribute(ServletParameter.USERNAME_ATTRIBUTE);
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals(ServletParameter.EMPTY_PATH)) {
            Long userId = (Long) session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE);
            logger.info("User {} (ID: {}) viewing bookings", username, userId);

            try {
                List<BookingModel> bookingList = bookingService.getUserBookings(userId);
                logger.info("Found {} bookings for user {}", bookingList.size(), username);
                request.setAttribute(ServletParameter.BOOKING_ATTRIBUTE, bookingList);
                request.getRequestDispatcher(ServletParameter.BOOKINGS_JSP).forward(request, response);
            } catch (ServiceException e) {
                logger.error("Failed to get bookings for user {}: {}", username, e.getMessage());
                request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Failed to load bookings");
                request.getRequestDispatcher(ServletParameter.BOOKINGS_JSP).forward(request, response);
            }

        } else if (pathInfo.equals(ServletParameter.NEW_PATH)) {
            logger.info("User {} accessing new booking form", username);
            request.getRequestDispatcher(ServletParameter.NEW_BOOKING_JSP).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        logger.info("POST /bookings request from IP: {}", clientIp);

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(ServletParameter.USER_ATTRIBUTE) == null) {
            logger.warn("Unauthorized booking creation attempt from IP: {}", clientIp);
            try {
                response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_LOGIN_PATH);
            } catch (IOException e) {
                logger.error("Failed to send redirect to login page", e);
            }
            return;
        }

        Long userId = (Long) session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE);
        String username = (String) session.getAttribute(ServletParameter.USERNAME_ATTRIBUTE);

        try {
            String petTypeStr = request.getParameter(ServletParameter.PET_TYPE_PARAM);
            String[] serviceStringList = request.getParameterValues(ServletParameter.SERVICES_PARAM);
            String preferredDateString = request.getParameter(ServletParameter.PREFERRED_DATE_PARAM);
            String notes = request.getParameter(ServletParameter.NOTES_PARAM);

            if (petTypeStr == null || petTypeStr.isBlank()) {
                throw new IllegalArgumentException("Pet type is required");
            }
            if (serviceStringList == null || serviceStringList.length == 0) {
                throw new IllegalArgumentException("At least one service must be selected");
            }
            if (preferredDateString == null || preferredDateString.isBlank()) {
                throw new IllegalArgumentException("Preferred date is required");
            }

            PetType petType = PetType.valueOf(petTypeStr.toUpperCase());
            List<ServiceType> serviceList = Arrays.stream(serviceStringList)
                    .map(String::toUpperCase)
                    .map(ServiceType::valueOf)
                    .collect(Collectors.toList());
            LocalDateTime preferredDate = LocalDateTime.parse(preferredDateString, DATE_TIME_FORMATTER);

            logger.info("User {} creating booking - Pet: {}, Services: {}, Date: {}",
                    username, petType, serviceList.size(), preferredDate);

            BookingModel booking = bookingService.createBooking(
                    userId, petType, serviceList, preferredDate, notes
            );

            logger.info("Booking created successfully - ID: {} for user {}",
                    booking.getBookingId(), username);

            response.sendRedirect(request.getContextPath() + ServletParameter.BOOKINGS_PATH_PREFIX + booking.getBookingId());

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid booking data from user {}: {}", username, e.getMessage());
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, e.getMessage());
            request.getRequestDispatcher(ServletParameter.NEW_BOOKING_JSP).forward(request, response);

        } catch (ServiceException e) {
            logger.error("Failed to create booking for user {}: {}", username, e.getMessage());
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Failed to create booking: " + e.getMessage());
            request.getRequestDispatcher(ServletParameter.NEW_BOOKING_JSP).forward(request, response);
        }
    }
}