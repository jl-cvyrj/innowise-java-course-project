package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static by.paulouskaya.webproject.util.UserGeneratorId.generateUserId;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(SighUpServlet.class);

    private LoginService loginService;

    public void init() {
        loginService = new LoginService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("GET request to login page from IP: " + request.getRemoteAddr());
        request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");  // гэта можа быць username або email
        String password = request.getParameter("password");

        // Лагаванне атрыманых дадзеных (без пароля)
        logger.info("Login attempt for username/email: " + username);

        // 2. Валідацыя дадзеных
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Empty username in login attempt");
            request.setAttribute("error", "Please enter username or email");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            logger.warn("Empty password for user: " + username);
            request.setAttribute("error", "Please enter password");
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
            return;
        }
    }
}
