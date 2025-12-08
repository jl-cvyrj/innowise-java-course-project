package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.model.UserRole;
import by.paulouskaya.webproject.service.impl.LoginServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(LoginServlet.class);

    private static final String USER_ATTRIBUTE = "user";
    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String USERNAME_ATTRIBUTE = "username";
    private static final String USER_ROLE_ATTRIBUTE = "userRole";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String PRESERVED_USERNAME_ATTRIBUTE = "preservedUsername";
    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";

    private static final String LOGIN_JSP = "/pages/login.jsp";
    private static final String DASHBOARD_PATH = "/dashboard";
    private static final String ADMIN_DASHBOARD_PATH = "/admin/dashboard";

    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60;

    private LoginServiceImpl loginServiceImpl;

    public void init() {
        loginServiceImpl = new LoginServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to login page from IP: {}", request.getRemoteAddr());

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(USER_ATTRIBUTE) != null) {
            logger.info("User already logged in, redirecting to dashboard");
            response.sendRedirect(DASHBOARD_PATH);
            return;
        }
        logger.info("Showing login form for unauthenticated user");
        request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter(USERNAME_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);

        logger.info("Login attempt for username/email: {}", username);

        if (username == null || username.isBlank()) {
            logger.warn("Empty username in login attempt");
            request.setAttribute(ERROR_ATTRIBUTE, "Please enter username or email");
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        if (password == null || password.isBlank()) {
            logger.warn("Empty password for user: {}", username);
            request.setAttribute(ERROR_ATTRIBUTE, "Please enter password");
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
            return;
        }

        try {
            UserModel user = loginServiceImpl.authenticate(username, password);
            HttpSession session = request.getSession();
            session.setAttribute(USER_ATTRIBUTE, user);
            session.setAttribute(USER_ID_ATTRIBUTE, user.getUserId());
            session.setAttribute(USERNAME_ATTRIBUTE, user.getUserName());
            session.setAttribute(USER_ROLE_ATTRIBUTE, user.getRole());

            session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
            logger.info("Successful login for user: {} (ID: {})",
                    user.getUserName(), user.getUserId());

            String redirectPath;
            if (user.getRole() == UserRole.ADMIN) {
                redirectPath = ADMIN_DASHBOARD_PATH;
                logger.info("Admin user logged in, redirecting to admin dashboard");
            } else {
                redirectPath = DASHBOARD_PATH;
                logger.info("Client user logged in, redirecting to user dashboard");
            }
            response.sendRedirect(request.getContextPath() + redirectPath);
        } catch (ServiceException e) {
            logger.warn("Failed login attempt for username: {}", username);
            request.setAttribute(ERROR_ATTRIBUTE, "Invalid username or password");
            request.setAttribute(PRESERVED_USERNAME_ATTRIBUTE, username);
            request.getRequestDispatcher(LOGIN_JSP).forward(request, response);
        }
    }
}