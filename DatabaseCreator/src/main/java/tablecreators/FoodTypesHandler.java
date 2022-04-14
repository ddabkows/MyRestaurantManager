package tablecreators;


import databaseparams.Categories;
import databaseparams.ColumnNames;
import databaseparams.TablesNames;
import org.junit.platform.commons.logging.LoggerFactory;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;


public class FoodTypesHandler extends ConnectionHandler {
    public FoodTypesHandler() throws SQLException {
        String eraseQuery = String.format("DROP TABLE %s", TablesNames.PRODUCT);
        realizeUpdateQuery(eraseQuery);
        eraseQuery = String.format("DROP TABLE %s", TablesNames.CATEGORY);
        realizeUpdateQuery(eraseQuery);
        createTable();
        addCategories();
        showTable();
    }

    @Override
    public void createTable() {
        String createQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                id int UNSIGNED AUTO_INCREMENT,
                %s VARCHAR(64) NOT NULL UNIQUE,
                PRIMARY KEY (id));
                """, TablesNames.CATEGORY, ColumnNames.CATEGORYCOL);
        realizeUpdateQuery(createQuery);
    }

    public void addCategories() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add(Categories.STARTERS.toString());
        categories.add(Categories.URUMAKIS.toString());
        categories.add(Categories.MAKIS.toString());
        categories.add(Categories.NIGIRIS.toString());
        for (String category : categories) {
            String insertQuery = String.format("""
                    INSERT IGNORE INTO %s (%s)
                    VALUES ('%s')""", TablesNames.CATEGORY, ColumnNames.CATEGORYCOL, category);
            realizeUpdateQuery(insertQuery);
        }
    }

    public void showTable() throws SQLException {
        String selectQuery = String.format("""
                SELECT *
                FROM %s""", TablesNames.CATEGORY);
        ResultSet queryResult = executeSelectQuery(selectQuery);
        while (queryResult.next()) {
            String category = queryResult.getString(ColumnNames.CATEGORYCOL.toString());
            Supplier<String> categorySupplier = ()-> "Category : " + category;
            LoggerFactory.getLogger(FoodTypesHandler.class).info(categorySupplier);
        }
    }
}
