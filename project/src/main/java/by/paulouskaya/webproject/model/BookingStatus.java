package by.paulouskaya.webproject.model;

public enum BookingStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    IN_PROGRESS("In progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED || this == REJECTED;
    }
}