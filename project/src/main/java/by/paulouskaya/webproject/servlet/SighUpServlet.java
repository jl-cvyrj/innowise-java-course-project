package by.paulouskaya.webproject.servlet;

import by.paulouskaya.webproject.model.UserModel;
import by.paulouskaya.webproject.service.SighUpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static by.paulouskaya.webproject.util.UserGeneratorId.generateUserId;

@WebServlet(name = "SighUpServlet", value = "/sighup")
public class SighUpServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(SighUpServlet.class);

    private SighUpService sighUpServiceService;

    public void init() {
        sighUpServiceService = new SighUpService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/sighup.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (username == null || username.isBlank()) {
            logger.warn("Empty username provided");
            request.setAttribute("error", "Username cannot be empty");
            request.getRequestDispatcher("/pages/signup.jsp").forward(request, response);
            return;
        }

        if (email == null || email.isBlank()) {
            logger.warn("Empty email provided");
            request.setAttribute("error", "Email cannot be empty");
            request.getRequestDispatcher("/pages/signup.jsp").forward(request, response);
            return;
        }

        if (password == null || password.isBlank()) {
            logger.warn("Empty password provided");
            request.setAttribute("error", "Password cannot be empty");
            request.getRequestDispatcher("/pages/signup.jsp").forward(request, response);
            return;
        }
        Long userId = generateUserId();
        UserModel userModel = new UserModel(userId, username, email, password, role);

        logger.info("Attempting to register user: ID={}, username={}", userId, username);

    }
}
