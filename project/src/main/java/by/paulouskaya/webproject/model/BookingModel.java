package by.paulouskaya.webproject.model;

import by.paulouskaya.webproject.util.BookingGeneratorId;

import java.time.LocalDateTime;
import java.util.List;

public class BookingModel {
    private static final String COMMA_SEPARATOR = ", ";
    private static final String EMPTY_STRING = "";
    private static final String STRING_BUFFER_START = "BookingModel{";
    private static final String BOOKING_ID_FIELD = "bookingId=";
    private static final String USER_ID_FIELD = ", userId=";
    private static final String PET_TYPE_FIELD = ", petType=";
    private static final String SERVICES_FIELD = ", services=";
    private static final String PREFERRED_DATE_FIELD = ", preferredDate=";
    private static final String STATUS_FIELD = ", status=";
    private static final String NOTES_FIELD = ", notes='";
    private static final char SINGLE_QUOTE = '\'';
    private static final char CLOSING_BRACE = '}';

    private Long bookingIdGenerated = BookingGeneratorId.generateBookingId();
    private Long bookingId;
    private Long userId;
    private PetType petType;
    private List<ServiceType> services;
    private LocalDateTime preferredDate;
    private BookingStatus status;
    private String notes;

    public BookingModel(Long userId, PetType petType,
                        List<ServiceType> services, LocalDateTime preferredDate,
                        BookingStatus status, String notes) {
        this.bookingId = bookingIdGenerated;
        this.userId = userId;
        this.petType = petType;
        this.services = services;
        this.preferredDate = preferredDate;
        this.status = status;
        this.notes = notes;
    }

    public Long getBookingId() { return bookingId; }
    public Long getUserId() { return userId; }
    public PetType getPetType() { return petType; }
    public List<ServiceType> getServices() { return services; }
    public LocalDateTime getPreferredDate() { return preferredDate; }
    public BookingStatus getStatus() { return status; }
    public String getNotes() { return notes; }

    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setPetType(PetType petType) { this.petType = petType; }
    public void setServices(List<ServiceType> services) { this.services = services; }
    public void setPreferredDate(LocalDateTime preferredDate) { this.preferredDate = preferredDate; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getServicesAsString() {
        if (services == null || services.isEmpty()) {
            return "";
        }
        return services.stream()
                .map(ServiceType::getDisplayName)
                .reduce((a, b) -> a + COMMA_SEPARATOR + b)
                .orElse(EMPTY_STRING);
    }

    @Override
    public String toString() {
        final StringBuffer stringBuffer = new StringBuffer(STRING_BUFFER_START);
        stringBuffer.append(BOOKING_ID_FIELD).append(bookingId);
        stringBuffer.append(USER_ID_FIELD).append(userId);
        stringBuffer.append(PET_TYPE_FIELD).append(petType);
        stringBuffer.append(SERVICES_FIELD).append(services);
        stringBuffer.append(PREFERRED_DATE_FIELD).append(preferredDate);
        stringBuffer.append(STATUS_FIELD).append(status);
        stringBuffer.append(NOTES_FIELD).append(notes).append(SINGLE_QUOTE);
        stringBuffer.append(CLOSING_BRACE);
        return stringBuffer.toString();
    }
}