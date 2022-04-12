package dao;

import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public enum DBConnection {
    INSTANCE;

    private Connection connection;

    DBConnection() {
        try {
            System.out.println("HELLO");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantmanager",
                    "restaurantmanager",
                    "1013mintok14n");
        } catch (SQLException throwables) {
            Supplier<String> error = throwables::getMessage;
            LoggerFactory.getLogger(DBConnection.class).error(error);
        }
    }

    public Connection getConnection() {return connection;}
}
