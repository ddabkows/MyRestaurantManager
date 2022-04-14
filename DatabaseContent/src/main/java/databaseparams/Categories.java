package databaseparams;

public enum Categories {
    STARTERS(12) {public final String toString() {return "Starters";}},
    URUMAKIS(12) {public final String toString() {return "Urumakis";}},
    MAKIS(12) {public final String toString() {return "Makis";}},
    NIGIRIS(12) {public final String toString() {return "Nigiris";}};

    private final int tax;

    Categories(int taxToSet) {this.tax = taxToSet;}

    public int getTax() {return this.tax;}
}
