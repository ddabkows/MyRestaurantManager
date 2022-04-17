package databaseparams;

public enum Nigiris {
    SALMON("Nigiri with salmon, avocado, sesame and Edamame beans", 15.f) {public final String toString() {return "Salmon Nigiri";}},
    TUNA("Nigiri with salmon, avocado, sesame and Edamame beans", 19.f) {public final String toString() {return "Tuna Nigiri";}},
    SALMONTUNA("Nigiri with salmon, tuna, avocado, sesame and Edamame beans", 17.f) {public final String toString() {return "Salmon and tuna Nigiri";}};

    private final String description;
    private final float price;

    Nigiris(String descriptionToSet, float priceToSet) {
        this.description = descriptionToSet;
        this.price = priceToSet;
    }

    public String getDescription() {return this.description;}
    public float getPrice() {return this.price;}
}
