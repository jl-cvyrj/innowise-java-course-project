package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.DaoException;
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
    public void init() throws ServletException {
        try {
            bookingService = new BookingServiceImpl();
        } catch (DaoException e) {
            throw new ServletException("Failed to initialize BookingService", e);
        }
    }

    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(ServletParameter.USER_ATTRIBUTE) == null) {
            response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_LOGIN_PATH);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkSession(request, response)) return;

        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE);
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals(ServletParameter.EMPTY_PATH)) {
            try {
                List<BookingModel> bookings = bookingService.findUserBookings(userId);
                request.setAttribute("booking", bookings);  // выкарыстоўваем менавіта "booking"
                request.getRequestDispatcher("/pages/booking.jsp").forward(request, response);
            } catch (ServiceException e) {
                logger.error("Failed to get bookings: {}", e.getMessage());
                request.setAttribute("error", "Failed to load bookings");
                request.getRequestDispatcher("/pages/booking.jsp").forward(request, response);
            }
        }
        else if (pathInfo.equals(ServletParameter.NEW_PATH)) {
            response.sendRedirect(request.getContextPath() + "/booking");
        }
        else if (pathInfo.startsWith("/edit/")) {
            Long bookingId = Long.parseLong(pathInfo.substring("/edit/".length()));
            try {
                BookingModel booking = bookingService.findBookingById(bookingId);
                request.setAttribute(ServletParameter.BOOKING_ATTRIBUTE, booking);
                request.getRequestDispatcher(ServletParameter.EDIT_BOOKING_JSP).forward(request, response);
            } catch (ServiceException e) {
                logger.error("Failed to load booking for edit: {}", e.getMessage());
                response.sendRedirect(request.getContextPath() + "/booking");
            }
        }
        else if (pathInfo.startsWith("/delete/")) {
            Long bookingId = Long.parseLong(pathInfo.substring("/delete/".length()));
            try {
                bookingService.deleteBooking(bookingId);
            } catch (ServiceException e) {
                logger.error("Failed to delete booking: {}", e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/booking");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkSession(request, response)) return;

        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE);

        try {
            PetType petType = PetType.valueOf(request.getParameter(ServletParameter.PET_TYPE_PARAMETER).toUpperCase());
            List<ServiceType> services = Arrays.stream(request.getParameterValues(ServletParameter.SERVICES_PARAMETER))
                    .map(ServiceType::fromDisplayName)
                    .collect(Collectors.toList());
            LocalDateTime date = LocalDateTime.parse(request.getParameter(ServletParameter.PREFERRED_DATE_PARAMETER), DATE_TIME_FORMATTER);
            String notes = request.getParameter(ServletParameter.NOTES_PARAMETER);

            BookingModel booking = bookingService.createBooking(userId, petType, services, date, notes);
            response.sendRedirect(request.getContextPath() + ServletParameter.BOOKINGS_PATH_PREFIX + booking.getBookingId());

        } catch (IllegalArgumentException e) {
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, e.getMessage());
            request.getRequestDispatcher(ServletParameter.NEW_BOOKING_JSP).forward(request, response);
        } catch (ServiceException e) {
            logger.error("Booking creation failed: {}", e.getMessage());
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Failed to create booking");
            request.getRequestDispatcher(ServletParameter.NEW_BOOKING_JSP).forward(request, response);
        }
    }
}