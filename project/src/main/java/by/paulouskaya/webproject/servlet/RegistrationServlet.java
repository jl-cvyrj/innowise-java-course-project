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

    public void init() {
        registrationServiceImpl = new RegistrationServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to registration page");
        request.getRequestDispatcher(ServletParameter.REGISTER_JSP).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter(ServletParameter.USERNAME_PARAMETER);
        String email = request.getParameter(ServletParameter.EMAIL_PARAMETER);
        String password = request.getParameter(ServletParameter.PASSWORD_PARAMETER);
        String roleString = request.getParameter(ServletParameter.ROLE_PARAMETER);

        UserRole role;
        if (roleString == null || roleString.isBlank()) {
            role = UserRole.CLIENT;
        } else {
            try {
                role = UserRole.valueOf(roleString.toUpperCase());
            } catch (IllegalArgumentException e) {
                role = UserRole.CLIENT;
            }
        }

        logger.info("Registration attempt for username: {}", username);

        if (username == null || username.isBlank()) {
            logger.warn("Empty username provided");
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Username cannot be empty");
            request.getRequestDispatcher(ServletParameter.SIGNUP_JSP).forward(request, response);
            return;
        }

        if (email == null || email.isBlank()) {
            logger.warn("Empty email provided");
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Email cannot be empty");
            request.getRequestDispatcher(ServletParameter.SIGNUP_JSP).forward(request, response);
            return;
        }

        if (password == null || password.isBlank()) {
            logger.warn("Empty password provided");
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Password cannot be empty");
            request.getRequestDispatcher(ServletParameter.SIGNUP_JSP).forward(request, response);
            return;
        }

        List<String> passwordErrors = PasswordValidator.validatePassword(password);
        if (!passwordErrors.isEmpty()) {
            String errorMessage = String.join(ServletParameter.COMMA_SEPARATOR, passwordErrors);
            setError(request, errorMessage, username, email);
            forwardToRegister(request, response);
            return;
        }

        try {
            UserModel user = registrationServiceImpl.register(username, email, password, role);

            logger.info("Successfully registered user: {} (ID: {})", username, user.getUserId());

            HttpSession session = request.getSession();
            session.setAttribute(ServletParameter.USER_ATTRIBUTE, user);
            session.setAttribute(ServletParameter.USER_ID_ATTRIBUTE, user.getUserId());
            session.setAttribute(ServletParameter.USERNAME_ATTRIBUTE, user.getUserName());
            session.setAttribute(ServletParameter.USER_ROLE_ATTRIBUTE, user.getRole());

            response.sendRedirect(request.getContextPath() + ServletParameter.DASHBOARD_PATH);
        } catch (ServiceException e) {
            logger.error("Database error during registration: {}", e.getMessage());
            setError(request, "System error. Please try again.", username, email);
            forwardToRegister(request, response);
        }
    }

    private void setError(HttpServletRequest request, String error,
                          String username, String email) {
        request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, error);
        request.setAttribute(ServletParameter.PRESERVED_USERNAME_ATTRIBUTE, username);
        request.setAttribute(ServletParameter.PRESERVED_EMAIL_ATTRIBUTE, email);
    }

    private void forwardToRegister(HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(ServletParameter.REGISTER_JSP).forward(request, response);
    }
}