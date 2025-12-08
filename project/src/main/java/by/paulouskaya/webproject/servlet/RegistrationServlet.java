package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.DaoException;
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

    private static final String USERNAME_PARAM = "username";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";
    private static final String ROLE_PARAM = "role";

    private static final String USER_ATTRIBUTE = "user";
    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String USERNAME_ATTRIBUTE = "username";
    private static final String USER_ROLE_ATTRIBUTE = "userRole";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String PRESERVED_USERNAME_ATTRIBUTE = "preservedUsername";
    private static final String PRESERVED_EMAIL_ATTRIBUTE = "preservedEmail";

    private static final String REGISTER_JSP = "/pages/register.jsp";
    private static final String SIGNUP_JSP = "/pages/signup.jsp";
    private static final String DASHBOARD_PATH = "/dashboard";

    private static final String COMMA_SEPARATOR = ", ";

    private RegistrationServiceImpl registrationServiceImpl;

    public void init() {
        registrationServiceImpl = new RegistrationServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to registration page");
        request.getRequestDispatcher(REGISTER_JSP).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter(USERNAME_PARAM);
        String email = request.getParameter(EMAIL_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        String roleString = request.getParameter(ROLE_PARAM);

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
            request.setAttribute(ERROR_ATTRIBUTE, "Username cannot be empty");
            request.getRequestDispatcher(SIGNUP_JSP).forward(request, response);
            return;
        }

        if (email == null || email.isBlank()) {
            logger.warn("Empty email provided");
            request.setAttribute(ERROR_ATTRIBUTE, "Email cannot be empty");
            request.getRequestDispatcher(SIGNUP_JSP).forward(request, response);
            return;
        }

        if (password == null || password.isBlank()) {
            logger.warn("Empty password provided");
            request.setAttribute(ERROR_ATTRIBUTE, "Password cannot be empty");
            request.getRequestDispatcher(SIGNUP_JSP).forward(request, response);
            return;
        }

        List<String> passwordErrors = PasswordValidator.validatePassword(password);
        if (!passwordErrors.isEmpty()) {
            String errorMessage = String.join(COMMA_SEPARATOR, passwordErrors);
            setError(request, errorMessage, username, email);
            forwardToRegister(request, response);
            return;
        }

        try {
            UserModel user = registrationServiceImpl.register(username, email, password, role);

            logger.info("Successfully registered user: {} (ID: {})", username, user.getUserId());

            HttpSession session = request.getSession();
            session.setAttribute(USER_ATTRIBUTE, user);
            session.setAttribute(USER_ID_ATTRIBUTE, user.getUserId());
            session.setAttribute(USERNAME_ATTRIBUTE, user.getUserName());
            session.setAttribute(USER_ROLE_ATTRIBUTE, user.getRole());

            response.sendRedirect(request.getContextPath() + DASHBOARD_PATH);
        } catch (ServiceException e) {
            logger.error("Database error during registration: {}", e.getMessage());
            setError(request, "System error. Please try again.", username, email);
            forwardToRegister(request, response);
        }
    }

    private void setError(HttpServletRequest request, String error,
                          String username, String email) {
        request.setAttribute(ERROR_ATTRIBUTE, error);
        request.setAttribute(PRESERVED_USERNAME_ATTRIBUTE, username);
        request.setAttribute(PRESERVED_EMAIL_ATTRIBUTE, email);
    }

    private void forwardToRegister(HttpServletRequest request,
                                   HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(REGISTER_JSP).forward(request, response);
    }
}