package restaurant.content;

public enum ProductTypes {
    STARTERS(1),
    DISHES(2),
    DESSERTS(3),
    DRINKS(4);
    private final int type;

    ProductTypes(int typeToSet) {
        this.type = typeToSet;
    }

    public int getType() {return this.type;}

}
