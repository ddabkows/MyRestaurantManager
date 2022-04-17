package databaseparams;

public enum Categories {
    STARTERS(12, Starters.values()) {public final String toString() {return "Starters";}},
    URUMAKIS(12, Urumakis.values()) {public final String toString() {return "Urumakis";}},
    MAKIS(12, Makis.values()) {public final String toString() {return "Makis";}},
    NIGIRIS(12, Nigiris.values()) {public final String toString() {return "Nigiris";}},
    SOFTS(21, Softs.values()) {public final String toString() {return "Softs";}};

    private final int tax;
    private final Enum[] components;

    Categories(int taxToSet, Enum[] componentsToSet) {
        this.tax = taxToSet;
        this.components = componentsToSet;
    }

    public int getTax() {return this.tax;}
    public Enum[] getComponents() {return this.components;}
}
