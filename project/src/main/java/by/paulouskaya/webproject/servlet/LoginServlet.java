package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.exception.DaoException;
import by.paulouskaya.webproject.exception.ServiceException;
import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.service.LoginService;
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
    private LoginService loginService;

    public void init() {
        loginService = new LoginService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to login page from IP: {}", request.getRemoteAddr());

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            logger.info("User already logged in, redirecting to dashboard");
            response.sendRedirect("/dashboard");
            return;
        }
        logger.info("Showing login form for unauthenticated user");
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");  // гэта можа быць username або email
        String password = request.getParameter("password");

        logger.info("Login attempt for username/email: {}", username);

        if (username == null || username.isBlank()) {
            logger.warn("Empty username in login attempt");
            request.setAttribute("error", "Please enter username or email");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.isBlank()) {
            logger.warn("Empty password for user: {}", username);
            request.setAttribute("error", "Please enter password");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        try {
            UserModel user = loginService.authenticate(username, password);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUserName());
            session.setAttribute("userRole", user.getRole());

            session.setMaxInactiveInterval(30 * 60);
            logger.info("Successful login for user: {} (ID: {})",
                    user.getUserName(), user.getUserId());

            String redirectPath;
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                redirectPath = "/admin/dashboard";
                logger.info("Admin user logged in, redirecting to admin dashboard");
            } else {
                redirectPath = "/dashboard";
                logger.info("Client user logged in, redirecting to user dashboard");
            }
            response.sendRedirect(request.getContextPath() + redirectPath);
        } catch (ServiceException e) {
            logger.warn("Failed login attempt for username: {}", username);
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("preservedUsername", username);
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        }
    }
}
