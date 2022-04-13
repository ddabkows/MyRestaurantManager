package main;

import databaseparams.TablesNames;
import tablecreators.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {
        new AdminTokenHandler();
        new TokenHandler();
        new FoodTypesHandler();
        new ProductsHandler();
    }
}
