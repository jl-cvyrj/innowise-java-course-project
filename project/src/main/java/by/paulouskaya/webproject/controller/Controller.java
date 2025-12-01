package by.paulouskaya.webproject.controller;

import by.paulouskaya.webproject.exception.ServletSalonException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ControllerServlet", value = "/controller")
public class Controller extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.getContentType();
        try {
            request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
        } catch (ServletException | IOException e) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            super.doPost(request, response);
        } catch (ServletException | IOException e) {

        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
