package restaurant;

public class Product {
    String name;
    int quantity;
    int type;
    double price;

    public Product(String nameToSet, int quantityToSet, int typeToSet, double priceToSet) {
        name = nameToSet;
        quantity = quantityToSet;
        type = typeToSet;
        price = priceToSet;
    }

    public String getName() {return this.name;}
    public int getQuantity() {return this.quantity;}
    public int getType() {return this.type;}
    public double getPrice() {return price;}
}
