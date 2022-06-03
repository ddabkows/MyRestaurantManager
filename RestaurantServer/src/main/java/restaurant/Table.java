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
    private final List<Product> starters = new ArrayList<>();
    private final List<Product> dishes = new ArrayList<>();
    private final List<Product> drinks = new ArrayList<>();
    private final List<Product> desserts = new ArrayList<>();

    public boolean isOpen() {return this.isOpen;}

    public boolean open(int peopleCountToSet) {
        if (tableLock.isLocked()) {
            return false;
        }  else if (tableBusy) {
            return false;
        } else if (peopleCountToSet == 0 && this.isOpen) {
            tableBusy = true;
            return true;
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
                starters.clear();
                dishes.clear();
                desserts.clear();
                drinks.clear();
                while (keys.hasNext()) {
                    String productName = keys.next();
                    JSONObject productSpecifics = productsJSONobj.getJSONObject(productName);
                    int productType = productSpecifics.getInt(TableValuesColumns.PRODUCTTYPE.toString());
                    double productPrice = productSpecifics.getDouble(TableValuesColumns.PRODUCTPRICE.toString());
                    String productComment = productSpecifics.getString(TableValuesColumns.PRODUCTCOMMENT.toString());
                    if (productType == ProductTypes.STARTERS.getType()) {
                        starters.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment));
                    }
                    else if (productType == ProductTypes.DISHES.getType()) {
                        dishes.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment));
                    }
                    else if (productType == ProductTypes.DESSERTS.getType()) {
                        desserts.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment));
                    }
                    else if (productType == ProductTypes.DRINKS.getType()) {
                        drinks.add(new Product(productName, productSpecifics.getInt(TableValuesColumns.PRODUCTQUANTITY.toString()), productType, productPrice, productComment));
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
            this.starters.clear();
            this.dishes.clear();
            this.desserts.clear();
            this.drinks.clear();
            this.isOpen = false;
            this.tableBusy = false;
            return true;
        } else {
            return false;
        }
    }
    public int getPeopleCount() {return this.peopleCount;}
    public void setPeopleCount(int peopleCountToSet) {this.peopleCount = peopleCountToSet;}
    public List<Product> getStarters() {return this.starters;}
    public List<Product> getDishes() {return this.dishes;}
    public List<Product> getDrinks() {return this.drinks;}
    public List<Product> getDesserts() {return this.desserts;}

    public boolean getTableBusy() {return this.tableBusy;}
    public void unbusyTable() {this.tableBusy = false;}
}
