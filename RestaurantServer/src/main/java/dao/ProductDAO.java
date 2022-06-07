package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO extends DAO {

    public int getID(String productName) {
        String query = String.format("""
                SELECT *
                FROM products
                WHERE name = '%s'""", productName);
        int id;
        try (ResultSet resultSet = realizeExecuteQuery(query)) {
            resultSet.next();
            id = resultSet.getInt("id");
        } catch (SQLException ignored) {
            id = -1;
        }
        return id;
    }
}
