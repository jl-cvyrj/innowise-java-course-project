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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet(name = "EditBookingServlet", value = "/booking/edit")
public class EditBookingServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(EditBookingServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(ServletParameter.DATE_TIME_PATTERN);

    private BookingService bookingService;

    @Override
    public void init() throws ServletException {
        try {
            bookingService = new BookingServiceImpl();
        } catch (Exception e) {
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

        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendRedirect(request.getContextPath() + "/booking");
            return;
        }

        try {
            Long bookingId = Long.parseLong(idParam);
            BookingModel booking = bookingService.findBookingById(bookingId);
            request.setAttribute("booking", booking);

            request.getRequestDispatcher("/pages/edit-booking.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/booking");
        } catch (ServiceException e) {
            logger.error("Failed to load booking for edit: {}", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/booking");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkSession(request, response)) return;

        try {
            Long bookingId = Long.parseLong(request.getParameter("bookingId"));
            PetType petType = PetType.valueOf(request.getParameter("petType").toUpperCase());

            String[] servicesArr = request.getParameterValues("services");
            List<ServiceType> services = new ArrayList<>();
            if (servicesArr != null) {
                services = Arrays.stream(servicesArr)
                        .map(ServiceType::fromDisplayName)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }

            LocalDateTime date = null;
            String dateStr = request.getParameter("preferredDate");
            if (dateStr != null && !dateStr.isBlank()) {
                date = LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);
            }

            String notes = request.getParameter("notes");

            BookingModel booking = bookingService.findBookingById(bookingId);
            booking.setPetType(petType);
            booking.setServices(services);
            booking.setPreferredDate(date);
            booking.setNotes(notes);

            bookingService.updateBooking(booking);

            response.sendRedirect(request.getContextPath() + "/booking");

        } catch (Exception e) {
            logger.error("Error updating booking: {}", e.getMessage(), e);
            request.setAttribute("error", "Failed to update booking");

            request.getRequestDispatcher("/pages/edit-booking.jsp").forward(request, response);
        }
    }
}