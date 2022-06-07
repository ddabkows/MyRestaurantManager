package tablecreators;

import databaseparams.ColumnNames;
import databaseparams.TablesNames;

import java.sql.SQLException;

public class ClosedTablesProductsHandler extends ConnectionHandler {

    public ClosedTablesProductsHandler() {
        createTable();
    }
    @Override
    public void createTable() {
        String createQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                id int UNSIGNED AUTO_INCREMENT,
                %s int UNSIGNED NOT NULL,
                %s int UNSIGNED NOT NULL,
                PRIMARY KEY (id),
                FOREIGN KEY (%s) REFERENCES %s(id),
                FOREIGN KEY (%s) REFERENCES %s(id))
                """, TablesNames.CLOSEDTABLESPRODUCTS, ColumnNames.PRODUCTFK, ColumnNames.TABLEFK, ColumnNames.PRODUCTFK, TablesNames.PRODUCT, ColumnNames.TABLEFK, TablesNames.CLOSEDTABLES);
        realizeUpdateQuery(createQuery);
    }
}
