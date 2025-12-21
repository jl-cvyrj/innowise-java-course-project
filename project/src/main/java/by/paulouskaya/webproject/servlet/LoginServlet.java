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

    private LoginServiceImpl loginServiceImpl;

    public void init() {
        loginServiceImpl = new LoginServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to login page from IP: {}", request.getRemoteAddr());

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(ServletParameter.USER_ATTRIBUTE) != null) {
            logger.info("User already logged in, redirecting to dashboard");
            response.sendRedirect(ServletParameter.DASHBOARD_PATH);
            return;
        }
        request.getRequestDispatcher(ServletParameter.LOGIN_JSP).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter(ServletParameter.USERNAME_PARAMETER);
        String password = request.getParameter(ServletParameter.PASSWORD_PARAMETER);

        logger.info("Login attempt for username/email: {}", username);

        if (username == null || username.isBlank()) {
            logger.warn("Empty username in login attempt");
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Please enter username or email");
            request.getRequestDispatcher(ServletParameter.LOGIN_JSP).forward(request, response);
            return;
        }

        if (password == null || password.isBlank()) {
            logger.warn("Empty password for user: {}", username);
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Please enter password");
            request.getRequestDispatcher(ServletParameter.LOGIN_JSP).forward(request, response);
            return;
        }

        try {
            UserModel user = loginServiceImpl.authenticate(username, password);
            HttpSession session = request.getSession();
            session.setAttribute(ServletParameter.USER_ATTRIBUTE, user);
            session.setAttribute(ServletParameter.USER_ID_ATTRIBUTE, user.getUserId());
            session.setAttribute(ServletParameter.USERNAME_ATTRIBUTE, user.getUserName());
            session.setAttribute(ServletParameter.USER_ROLE_ATTRIBUTE, user.getRole());

            session.setMaxInactiveInterval(ServletParameter.SESSION_TIMEOUT_SECONDS);
            logger.info("Successful login for user: {} (ID: {})",
                    user.getUserName(), user.getUserId());

            String redirectPath;
            if (user.getRole() == UserRole.ADMIN) {
                redirectPath = ServletParameter.ADMIN_DASHBOARD_PATH;
            } else {
                redirectPath = ServletParameter.DASHBOARD_PATH;
            }
            response.sendRedirect(request.getContextPath() + redirectPath);
        } catch (ServiceException e) {
            logger.warn("Failed login attempt for username: {}", username);
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Invalid username or password");
            request.setAttribute(ServletParameter.PRESERVED_USERNAME_ATTRIBUTE, username);
            request.getRequestDispatcher(ServletParameter.LOGIN_JSP).forward(request, response);
        }
    }
}