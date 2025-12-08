// PetType.java
package by.paulouskaya.webproject.model;

public enum PetType {
    DOG("Dog"),
    CAT("Cat");

    private final String displayName;

    PetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}