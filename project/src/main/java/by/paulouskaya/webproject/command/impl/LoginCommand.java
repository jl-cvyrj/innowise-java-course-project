package by.paulouskaya.webproject.command.impl;

import by.paulouskaya.webproject.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import static by.paulouskaya.webproject.command.CommandConstant.LOGIN;
import static by.paulouskaya.webproject.command.CommandConstant.PASSWORD;

public class LoginCommand implements Command {

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        return "pages/main.jsp";
    }
}
