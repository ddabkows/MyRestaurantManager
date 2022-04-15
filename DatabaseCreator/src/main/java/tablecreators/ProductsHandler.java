package tablecreators;

import databaseparams.*;
import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class ProductsHandler extends ConnectionHandler {
    public ProductsHandler() throws SQLException {
        createTable();
        addProducts();
        showTable();
    }

    @Override
    public void createTable() {
        String createQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                id int UNSIGNED AUTO_INCREMENT,
                %s VARCHAR(64) NOT NULL,
                %s int UNSIGNED NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (%s) REFERENCES %s(id));
                """, TablesNames.PRODUCT, ColumnNames.PRODUCTNAME, ColumnNames.CATEGORYCOL, ColumnNames.CATEGORYCOL, TablesNames.CATEGORY);
        realizeUpdateQuery(createQuery);
    }

    public void addProducts() throws SQLException {
        for (Categories category : Categories.values()) {
            addByCategory(category.getComponents());
        }
    }

    public void addByCategory(Enum[] category) throws SQLException {
        String getCategoryQuery = String.format("""
            SELECT id
            FROM %s
            WHERE %s = '%s'
            """, TablesNames.CATEGORY, ColumnNames.CATEGORYCOL, Categories.URUMAKIS);
        ResultSet categoryResultSet = executeSelectQuery(getCategoryQuery);
        categoryResultSet.next();
        int categoryID = categoryResultSet.getInt("id");
        for (Enum urumaki : category) {
            String insertQuery = String.format("""
                    INSERT IGNORE INTO %s (%s, %s)
                    VALUES ('%s', %d)
                    """, TablesNames.PRODUCT,
                    ColumnNames.PRODUCTNAME, ColumnNames.CATEGORYCOL, urumaki.toString(), categoryID);
            realizeUpdateQuery(insertQuery);
        }
    }

    public void showTable() throws SQLException {
        String selectQuery = String.format("""
                        SELECT %s.id, %s, %s.%s
                        FROM %s
                        INNER JOIN %s
                        ON %s.%s=%s.%s
                        """,
                TablesNames.PRODUCT, ColumnNames.PRODUCTNAME, TablesNames.CATEGORY, ColumnNames.CATEGORYCOL, TablesNames.PRODUCT,
                TablesNames.CATEGORY, TablesNames.PRODUCT, ColumnNames.CATEGORYCOL, TablesNames.CATEGORY, "id");
        ResultSet queryResult = executeSelectQuery(selectQuery);
        while (queryResult.next()) {
            String product = queryResult.getString(ColumnNames.PRODUCTNAME.toString());
            String category = queryResult.getString(String.format("%s.%s", TablesNames.CATEGORY, ColumnNames.CATEGORYCOL));
            Supplier<String> productSupplier = () -> "Product : " + product + " from category : " + category;
            LoggerFactory.getLogger(ProductsHandler.class).info(productSupplier);
        }
    }
}
