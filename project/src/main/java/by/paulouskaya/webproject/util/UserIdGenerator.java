package by.paulouskaya.webproject.util;

public class UserIdGenerator {
    private static long nextId = 1;

    private UserIdGenerator() {}

    public static synchronized long generateUserId() {
        return nextId++;
    }
}
