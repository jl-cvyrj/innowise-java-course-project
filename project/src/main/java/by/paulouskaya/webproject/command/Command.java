package by.paulouskaya.webproject.command;

import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    public String execute(HttpServletRequest request);
}
