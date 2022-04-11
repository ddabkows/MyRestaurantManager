package main;

import tablecreators.AdminTokenHandler;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, SQLException {
        new AdminTokenHandler();
    }
}
