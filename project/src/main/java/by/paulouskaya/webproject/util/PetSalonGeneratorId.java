package by.paulouskaya.webproject.util;

public enum PetSalonGeneratorId {
    USER, ORDER;
    private long nextUserId = 1;
    public long generateId() {
        return nextUserId++;
    }
}
