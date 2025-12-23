package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.BookingModel;
import by.paulouskaya.webproject.model.UserRole;
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
import java.util.List;

@WebServlet(name = "AdminBookingServlet", value = "/adminbooking/*")
public class AdminBookingServlet extends HttpServlet  {
  private static final Logger logger = LogManager.getLogger(AdminBookingServlet.class);

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
          DateTimeFormatter.ofPattern(ServletParameter.DATE_TIME_PATTERN);

  private BookingService bookingService;

  private boolean isAdmin(HttpSession session) {
    return session.getAttribute(ServletParameter.ROLE_PARAMETER).equals(UserRole.ADMIN.getDisplayName());
  }

  @Override
  public void init() {
    bookingService = new BookingServiceImpl();
    logger.info("BookingServlet initialized");
  }

  @Override
  protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute(ServletParameter.USER_ATTRIBUTE) == null) {
      logger.warn("Unauthorized access attempt to /adminbookings");
      response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_LOGIN_PATH);
      return;
    }

    String username = (String) session.getAttribute(ServletParameter.USERNAME_ATTRIBUTE);
    String pathInfo = request.getPathInfo();

    String role = (String) session.getAttribute(ServletParameter.ROLE_PARAMETER);
    if (!isAdmin(session)) {
      logger.warn("User {} tried to access admin page without permission", username);
      response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_ACCESS_DENIED_PATH);
      return;
    }

  Long userId = (Long) session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE);
    logger.info("User {} (ID: {}) viewing bookings", username, userId);

    try {
      List<BookingModel> allBookingList = bookingService.findAllBookings();
      logger.info("Found {} bookings", allBookingList.size());
      request.setAttribute(ServletParameter.BOOKING_ATTRIBUTE, allBookingList);
      request.getRequestDispatcher(ServletParameter.BOOKINGS_JSP).forward(request, response);
    } catch (ServiceException e) {
      logger.error("Failed to get bookings {}", e.getMessage());
      request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Failed to load bookings");
      request.getRequestDispatcher(ServletParameter.ADMIN_BOOKINGS_JSP).forward(request, response);
    }

  }

  @Override
  protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || !isAdmin(session)) {
      logger.warn("Unauthorized POST attempt to /adminbooking");
      response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_LOGIN_PATH);
      return;
    }

    String username = (String) session.getAttribute(ServletParameter.USERNAME_ATTRIBUTE);

    try {

      String bookingIdStr = request.getParameter(ServletParameter.BOOKING_ID_ATTRIBUTE);
      String action = request.getParameter(ServletParameter.ACTION_ATTRIBUTE);
      String dateTimeStr = request.getParameter(ServletParameter.ASSIGNED_DATA_ATTRIBUTE);

      if (bookingIdStr == null || bookingIdStr.isBlank()) {
        throw new IllegalArgumentException("Booking ID is required");
      }
      if (action == null || action.isBlank()) {
        throw new IllegalArgumentException("Action is required");
      }

      Long bookingId = Long.parseLong(bookingIdStr);

      switch (action) {
        case ServletParameter.ACTION_CONFIRM_ATTRIBUTE:
          bookingService.confirmBooking(bookingId);
          logger.info("Admin {} confirmed booking {}", username, bookingId);
          break;

        case ServletParameter.ACTION_REJECT_ATTRIBUTE:
          bookingService.rejectBooking(bookingId);
          logger.info("Admin {} rejected booking {}", username, bookingId);
          break;

        case ServletParameter.ACTION_COMPLETE_ATTRIBUTE:
          bookingService.completeBooking(bookingId);
          logger.info("Admin {} completed booking {}", username, bookingId);
          break;

        case ServletParameter.ACTION_SCHEDULE_ATTRIBUTE:
          LocalDateTime scheduleDate = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
          bookingService.scheduleBooking(bookingId, scheduleDate);
          logger.info("Admin {} scheduled booking {} at {}", username, bookingId, scheduleDate);
          break;

        case ServletParameter.ACTION_ASSIGN_DATE_TIME_ATTRIBUTE:
          LocalDateTime assignDate = LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
          bookingService.assignDateTime(bookingId, assignDate);
          logger.info("Admin {} assigned date/time {} to booking {}", username, assignDate, bookingId);
          break;

      }

    } catch (IllegalArgumentException e) {
      logger.warn("Invalid admin action by {}: {}", username, e.getMessage());
      request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, e.getMessage());
      request.getRequestDispatcher(ServletParameter.ADMIN_BOOKINGS_JSP).forward(request, response);

    } catch (ServiceException e) {
      logger.error("Failed to process admin action for {}: {}", username, e.getMessage());
      request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Failed to process action: " + e.getMessage());
      request.getRequestDispatcher(ServletParameter.ADMIN_BOOKINGS_JSP).forward(request, response);
    }

  }

}
