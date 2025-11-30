package by.paulouskaya.webproject.command.impl;

import by.paulouskaya.webproject.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "";
    }
}
