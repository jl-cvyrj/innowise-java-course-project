package by.paulouskaya.webproject.model;

public enum ServiceType {
    BATHING("Bathing", 25.0),
    HAIRCUT("Haircut", 35.0),
    NAIL_TRIMMING("Nail trimming", 15.0),
    EAR_CLEANING("Ear cleaning", 10.0),
    TEETH_BRUSHING("Teeth brushing", 20.0),
    FUR_TRIMMING("Fur trimming", 40.0),
    GROOMING("Full grooming", 60.0),
    PAW_CARE("Paw care", 12.0),
    FLEA_TREATMENT("Flea treatment", 30.0);

    private final String displayName;
    private final double price;

    ServiceType(String displayName, double price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPrice() {
        return price;
    }

    public static ServiceType fromString(String name) {
        try {
            return ServiceType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}