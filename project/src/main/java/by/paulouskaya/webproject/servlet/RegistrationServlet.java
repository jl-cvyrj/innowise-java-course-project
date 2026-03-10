package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.model.UserRole;
import by.paulouskaya.webproject.service.impl.RegistrationServiceImpl;
import by.paulouskaya.webproject.util.PasswordValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RegistrationServlet", value = "/register")
public class RegistrationServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(RegistrationServlet.class);

    private RegistrationServiceImpl registrationServiceImpl;

    @Override
    public void init() {
        registrationServiceImpl = new RegistrationServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET /register called");
        request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter(ServletParameter.USERNAME_PARAMETER);
        String email = request.getParameter(ServletParameter.EMAIL_PARAMETER);
        String password = request.getParameter(ServletParameter.PASSWORD_PARAMETER);
        String roleString = request.getParameter(ServletParameter.ROLE_PARAMETER);

        UserRole role;
        try {
            role = (roleString != null && !roleString.isBlank()) ? UserRole.valueOf(roleString.toUpperCase()) : UserRole.CLIENT;
        } catch (IllegalArgumentException e) {
            role = UserRole.CLIENT;
        }

        if (username == null || username.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "All fields are required");
            request.setAttribute(ServletParameter.PRESERVED_USERNAME_ATTRIBUTE, username);
            request.setAttribute(ServletParameter.PRESERVED_EMAIL_ATTRIBUTE, email);
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            return;
        }

        List<String> passwordErrors = PasswordValidator.validatePassword(password);
        if (!passwordErrors.isEmpty()) {
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, String.join(", ", passwordErrors));
            request.setAttribute(ServletParameter.PRESERVED_USERNAME_ATTRIBUTE, username);
            request.setAttribute(ServletParameter.PRESERVED_EMAIL_ATTRIBUTE, email);
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
            return;
        }

        try {
            UserModel user = registrationServiceImpl.register(username, email, password, role);

            HttpSession session = request.getSession();
            session.setAttribute(ServletParameter.USER_ATTRIBUTE, user);
            session.setAttribute(ServletParameter.USER_ID_ATTRIBUTE, user.getUserId());
            session.setAttribute(ServletParameter.USERNAME_ATTRIBUTE, user.getUserName());
            session.setAttribute(ServletParameter.USER_ROLE_ATTRIBUTE, user.getRole());
            session.setMaxInactiveInterval(ServletParameter.SESSION_TIMEOUT_SECONDS);

            logger.info("User {} registered successfully", username);
            response.sendRedirect(request.getContextPath() + ServletParameter.DASHBOARD_PATH);

        } catch (ServiceException e) {
            logger.error("Error during registration: {}", e.getMessage());
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "System error. Please try again.");
            request.setAttribute(ServletParameter.PRESERVED_USERNAME_ATTRIBUTE, username);
            request.setAttribute(ServletParameter.PRESERVED_EMAIL_ATTRIBUTE, email);
            request.getRequestDispatcher("/pages/register.jsp").forward(request, response);
        }
    }
}