package tablecreators;

import databaseparams.ColumnNames;
import databaseparams.TablesNames;
import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class ClosedTablesHandler extends ConnectionHandler {

    public ClosedTablesHandler() throws SQLException {
        String eraseQuery = String.format("DROP TABLE %s", TablesNames.CLOSEDTABLESPRODUCTS);
        realizeUpdateQuery(eraseQuery);
        eraseQuery = String.format("DROP TABLE %s", TablesNames.CLOSEDTABLES);
        realizeUpdateQuery(eraseQuery);
        createTable();
        showTable();
    }
    @Override
    public void createTable() {
        String createQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                id int UNSIGNED AUTO_INCREMENT,
                %s VARCHAR(64) NOT NULL,
                %s DATETIME NOT NULL,
                PRIMARY KEY (id));
                """, TablesNames.CLOSEDTABLES, ColumnNames.TABLENAME, ColumnNames.DATE);
        realizeUpdateQuery(createQuery);
    }

    public void showTable() throws  SQLException {
        String selectQuery = String.format("""
                SELECT *
                FROM %s
                """, TablesNames.CLOSEDTABLES);
        ResultSet queryResult = executeSelectQuery(selectQuery);
        if (queryResult.next()) {
            String name = queryResult.getString(ColumnNames.TABLENAME.toString());
            Supplier<String> tableSupplier = ()->name;
            LoggerFactory.getLogger(ClosedTablesHandler.class).info(tableSupplier);
        }
    }
}
