package by.paulouskaya.webproject.command;

import by.paulouskaya.webproject.command.impl.AddUserCommand;
import by.paulouskaya.webproject.command.impl.DefaultCommand;
import by.paulouskaya.webproject.command.impl.LoginCommand;
import by.paulouskaya.webproject.command.impl.LogoutCommand;

public enum CommandType {
    ADD_USER(new AddUserCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand());

    private final Command command;

    CommandType(Command command) {
        this.command = command;
    }

    public static Command parseCommand(String stringCommand) {
        try {
            CommandType commandType = CommandType.valueOf(stringCommand.toUpperCase());
            return commandType.command;
        } catch (IllegalArgumentException | NullPointerException e) {
            CommandType commandType = DEFAULT;
            return commandType.command;
        }
    }
}
