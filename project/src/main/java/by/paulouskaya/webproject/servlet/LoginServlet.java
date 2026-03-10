package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
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

    @Override
    public void init() {
        loginServiceImpl = new LoginServiceImpl();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET /login called from IP: {}", request.getRemoteAddr());

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(ServletParameter.USER_ATTRIBUTE) != null) {
            logger.info("User already logged in, redirecting to dashboard");
            response.sendRedirect(request.getContextPath() + ServletParameter.DASHBOARD_PATH);
            return;
        }

        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter(ServletParameter.USERNAME_PARAMETER);
        String password = request.getParameter(ServletParameter.PASSWORD_PARAMETER);

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Username and password cannot be empty");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
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

            logger.info("User {} logged in successfully", username);
            response.sendRedirect(request.getContextPath() + ServletParameter.DASHBOARD_PATH);

        } catch (ServiceException e) {
            logger.warn("Authentication failed for username {}: {}", username, e.getMessage());
            request.setAttribute(ServletParameter.ERROR_ATTRIBUTE, "Invalid username or password");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        }
    }
}