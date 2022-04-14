package databaseparams;

public enum Starters {
    EDAMAME("Boiled soybeans", 5.f) {public final String toString() {return "Edamame";}},
    GYOZA("Japanese dumplings with chicken, garlic and onions", 7.f) {public final String toString() {return "Gyoza";}},
    KARAAGE("Japanese style nuggets", 5.f) {public final String toString() {return "Karaage";}};

    private final String description;
    private final float price;

    Starters(String descriptionToSet, float priceToSet) {
        this.description = descriptionToSet;
        this.price = priceToSet;
    }

    public String getDescription() {return this.description;}
}
