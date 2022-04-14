package databaseparams;

public enum Urumakis {
    SALMON("Urumaki with salmon, avocado and sesame", 6.f)  {public final String toString() {return "Salmon Urumaki";}},
    TUNA("Urumaki with tuna, avocado, sesame and spicy sauce", 7.f) {public final String toString() {return "Tuna Urumaki";}},
    CALIFORNIA("Urumaki with mayonnaise, surimi, avocado and masago", 8.f) {public final String toString() {return "California Urumaki";}};

    private final String description;
    private final float price;

    Urumakis(String descriptionToSet, float priceToSet) {
        this.description = descriptionToSet;
        this.price = priceToSet;
    }

    public String getDescription() {return this.description;}
    public float getPrice() {return this.price;}
}
