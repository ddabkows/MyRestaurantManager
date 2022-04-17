package databaseparams;

public enum Softs {
    STILLWATERHALF(3.f) {public final String toString() {return "Still water 0.5L";}},
    SPARKLINGWATERHALF(3.f) {public final String toString() {return "Sparkling water 0.5L";}},
    COKE(3.5f) {public final String toString() {return "Coke";}},
    COKEZERO(3.5f) {public final String toString() {return "Coke zero";}},
    STILLWATER(5.f) {public final String toString() {return "Still water 1L";}},
    SPARKLINGWATER(5.f) {public final String toString() {return "Sparkling water 1L";}};

    private final float price;

    Softs(float priceToSet) {this.price = priceToSet;}

    public float getPrice() {return this.price;}
}
