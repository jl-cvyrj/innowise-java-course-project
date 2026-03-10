package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.*;
import by.paulouskaya.webproject.dao.impl.BookingDaoJdbcImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "NewBookingServlet", value = "/booking/new")
public class NewBookingServlet extends HttpServlet {

    private BookingDaoJdbcImpl bookingDao;

    @Override
    public void init() {
        bookingDao = new BookingDaoJdbcImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/pages/new-booking.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE) == null) {
            response.sendRedirect(request.getContextPath() + ServletParameter.REDIRECT_LOGIN_PATH);
            return;
        }

        Long userId = (Long) session.getAttribute(ServletParameter.USER_ID_ATTRIBUTE);

        String petTypeStr = request.getParameter(ServletParameter.PET_TYPE_PARAMETER);
        String[] servicesArr = request.getParameterValues(ServletParameter.SERVICES_PARAMETER);
        String preferredDateStr = request.getParameter(ServletParameter.PREFERRED_DATE_PARAMETER);
        String notes = request.getParameter(ServletParameter.NOTES_PARAMETER);

        PetType petType = petTypeStr != null ? PetType.valueOf(petTypeStr.toUpperCase()) : null;

        List<ServiceType> services = new ArrayList<>();
        if (servicesArr != null) {
            for (String s : servicesArr) {
                services.add(ServiceType.valueOf(s.toUpperCase()));
            }
        }

        LocalDateTime preferredDate = null;
        if (preferredDateStr != null && !preferredDateStr.isBlank()) {
            preferredDate = LocalDateTime.parse(preferredDateStr, DateTimeFormatter.ofPattern(ServletParameter.DATE_TIME_PATTERN));
        }

        BookingModel booking = new BookingModel(userId, petType, services, preferredDate, BookingStatus.PENDING, notes);

        try {
            bookingDao.save(booking);
            response.sendRedirect(request.getContextPath() + "/booking");
        } catch (DaoException e) {
            throw new ServletException("Error saving booking", e);
        }
    }
}