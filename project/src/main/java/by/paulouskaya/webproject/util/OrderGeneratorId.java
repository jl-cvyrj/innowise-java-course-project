package by.paulouskaya.webproject.util;

public class OrderGeneratorId {
    private static long nextUserId = 1;
    public static long generateOrderId() {
        nextUserId++;
        return nextUserId;
    }
}
