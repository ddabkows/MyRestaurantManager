package restaurant;

public class Product {
    String name;
    int quantity;
    int type;
    double price;
    String comment;
    double tax;

    public Product(String nameToSet, int quantityToSet, int typeToSet, double priceToSet, String commentToSet, double taxToSet) {
        name = nameToSet;
        quantity = quantityToSet;
        type = typeToSet;
        price = priceToSet;
        comment = commentToSet;
        tax = taxToSet;
    }

    public String getName() {return this.name;}
    public int getQuantity() {return this.quantity;}
    public int getType() {return this.type;}
    public double getPrice() {return price;}
    public String getComment() {return comment;}
    public double getTax() {return tax;}
}
