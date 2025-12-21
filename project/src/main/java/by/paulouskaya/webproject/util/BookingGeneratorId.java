package by.paulouskaya.webproject.util;

public class BookingGeneratorId {
    private static long nextBookingId = 1;
    public static long generateBookingId() {
        nextBookingId++;
        return nextBookingId;
    }
}
