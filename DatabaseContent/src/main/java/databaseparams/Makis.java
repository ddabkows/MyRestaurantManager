package databaseparams;

public enum Makis {
    SALMON("Maki with salmon", 5.f) {public final String toString() {return "Salmon Maki";}},
    TUNA("Maki with tuna", 6.f) {public final String toString() {return "Tuna Maki";}};

    private final String description;
    private final float price;

    Makis(String descriptionToSet, float priceToSet) {
        this.description = descriptionToSet;
        this.price = priceToSet;
    }

    public String getDescription() {return this.description;}
    public float getPrice() {return this.price;}
}
