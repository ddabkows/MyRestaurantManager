package tablecreators;

import databaseparams.Categories;
import databaseparams.ColumnNames;
import databaseparams.Starters;
import databaseparams.TablesNames;
import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        addStarters();
    }

    public void addStarters() throws SQLException {
        ArrayList<String> starters = new ArrayList<>();
        starters.add(Starters.EDAMAME.toString());
        starters.add(Starters.GYOZA.toString());
        starters.add(Starters.KARAAGE.toString());
        String getCategoryQuery = String.format("SELECT id FROM %s WHERE %s = '%s'", TablesNames.CATEGORY, ColumnNames.CATEGORYCOL, Categories.STARTERS);
        ResultSet categoryResultSet = executeSelectQuery(getCategoryQuery);
        categoryResultSet.next();
        int categoryID = categoryResultSet.getInt("id");
        for (String starter : starters) {
            String insertQuery = String.format("INSERT IGNORE INTO %s (%s, %s) VALUES ('%s', %d)", TablesNames.PRODUCT,
                    ColumnNames.PRODUCTNAME, ColumnNames.CATEGORYCOL, starter, categoryID);
            realizeUpdateQuery(insertQuery);
        }
    }

    public void showTable() throws SQLException {
        String selectQuery = String.format("SELECT %s.id, %s, %s.%s FROM %s INNER JOIN %s ON %s.%s=%s.%s",
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
