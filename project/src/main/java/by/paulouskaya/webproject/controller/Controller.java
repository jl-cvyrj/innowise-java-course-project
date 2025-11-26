package by.paulouskaya.webproject.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpRequest;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class Controller extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        request.getRequestDispatcher("/pages/result.jsp");
    }
}
