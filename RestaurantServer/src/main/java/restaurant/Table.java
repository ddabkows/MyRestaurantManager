package restaurant;

import org.json.JSONObject;
import packettypes.TableValuesColumns;
import restaurant.content.ProductTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
    ReentrantLock tableLock = new ReentrantLock();
    private boolean isOpen = false;
    private int peopleCount;
    private boolean tableBusy = false;
    private final List<Product> Starters = new ArrayList<>();
    private final List<Product> Dishes = new ArrayList<>();
    private final List<Product> Drinks = new ArrayList<>();
    private final List<Product> Desserts = new ArrayList<>();

    public boolean isOpen() {return this.isOpen;}

    public boolean open(int peopleCountToSet, String message) {
        if (tableLock.isLocked()) {
            return false;
        } else if (peopleCountToSet == 0 && this.isOpen) {
            return true;
        } else if (tableBusy) {
            return false;
        } else if (peopleCountToSet > 0 && !this.isOpen) {
            tableBusy = true;
            tableLock.lock();
            try {
                this.isOpen = true;
                peopleCount = peopleCountToSet;
            } finally {
                tableLock.unlock();
            }
            return true;
        } else {
            message = "Another user has this table opened";
        }
        return false;
    }

    public boolean set(int peopleCountToSet, JSONObject productsJSONobj) {
        if (tableLock.isLocked()) {
            return false;
        }
        else if (!this.isOpen) {
            return false;
        }
        else {
            tableLock.lock();
            try {
                setPeopleCount(peopleCountToSet);
                Iterator<String> keys = productsJSONobj.keys();
                Starters.clear();
                Dishes.clear();
                Desserts.clear();
                Drinks.clear();
                while (keys.hasNext()) {
                    String productName = keys.next();
                    JSONObject productSpecifics = productsJSONobj.getJSONObject(productName);
                    int productType = productSpecifics.getInt(TableValuesColumns.PRODUCTTYPE.toString());
                    double productPrice = productSpecifics.getDouble(TableValuesColumns.PRODUCTPRICE.toString());
                    if (productType == ProductTypes.STARTERS.getType()) {
                        Starters.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice));
                    }
                    else if (productType == ProductTypes.DISHES.getType()) {
                        Dishes.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice));
                    }
                    else if (productType == ProductTypes.DESSERTS.getType()) {
                        Desserts.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice));
                    }
                    else if (productType == ProductTypes.DRINKS.getType()) {
                        Drinks.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice));
                    }
                }
            } finally {
                tableLock.unlock();
            }
            tableBusy = false;
            return true;
        }
    }

    public boolean close() {
        if (this.isOpen) {
            this.isOpen = false;
            return true;
        } else {
            return false;
        }
    }
    public int getPeopleCount() {return this.peopleCount;}
    public void setPeopleCount(int peopleCountToSet) {this.peopleCount = peopleCountToSet;}
    public List<Product> getStarters() {return this.Starters;}
    public List<Product> getDishes() {return this.Dishes;}
    public List<Product> getDrinks() {return this.Drinks;}
    public List<Product> getDesserts() {return this.Desserts;}
}
