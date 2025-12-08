// PetType.java
package by.paulouskaya.webproject.model;

public enum UserRole {
    ADMIN("Admin"),
    CLIENT("Client");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}