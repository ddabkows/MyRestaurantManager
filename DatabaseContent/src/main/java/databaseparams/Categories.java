package databaseparams;

public enum Categories {
    STARTERS(12, Starters.values()) {public final String toString() {return "Starters";}},
    URUMAKIS(12, Urumakis.values()) {public final String toString() {return "Urumakis";}},
    MAKIS(12, Starters.values()) {public final String toString() {return "Makis";}},
    NIGIRIS(12, Starters.values()) {public final String toString() {return "Nigiris";}};

    private final int tax;
    private final Enum[] components;

    Categories(int taxToSet, Enum[] componentsToSet) {
        this.tax = taxToSet;
        this.components = componentsToSet;
    }

    public int getTax() {return this.tax;}
    public Enum[] getComponents() {return this.components;}
}
