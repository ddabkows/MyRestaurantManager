package main;

import tablecreators.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {
        AdminTokenHandler adminTokenHandler = new AdminTokenHandler();
        Statement stmt = adminTokenHandler.connection.createStatement();
        stmt.execute("SET FOREIGN_KEY_CHECKS=0");
        stmt.close();
        new TokenHandler();
        new FoodTypesHandler();
        new ProductsHandler();
        new ClosedTablesHandler();
        new ClosedTablesProductsHandler();
        adminTokenHandler.realizeUpdateQuery("SET foreign_key_checks = 1;");
    }
}
