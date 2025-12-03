package by.paulouskaya.webproject.util;

public class UserGeneratorId {
    private static long nextId = 1;
    public static long generateUserId() {
        return nextId++;
    }
}
