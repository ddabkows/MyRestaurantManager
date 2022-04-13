package tablecreators;


import databaseparams.ColumnNames;
import databaseparams.TablesNames;
import org.junit.platform.commons.logging.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

/**Method responsible for creating the admin_token table
 * and adding the admintoken
 */
public class AdminTokenHandler extends ConnectionHandler {
    public AdminTokenHandler() throws NoSuchAlgorithmException, SQLException {
        String eraseQuery = String.format("DROP TABLE %s", TablesNames.ADMINTOKEN);
        realizeUpdateQuery(eraseQuery);
        createTable();
        addAdminToken();
        showTable();
    }

    /**Creates the admin_token table
     *
     */
    @Override
    public void createTable() {
        String createQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                id int UNSIGNED AUTO_INCREMENT,
                %s VARCHAR(64) NOT NULL UNIQUE,
                PRIMARY KEY (id));""", TablesNames.ADMINTOKEN, ColumnNames.ADMINTOKENCOL);
        realizeUpdateQuery(createQuery);
    }

    /**Adds the admin token
     *
     */
    public void addAdminToken() throws NoSuchAlgorithmException {
        String adminToken = encrypt("1011str1012t1013mintok14n");
        String insertQuery = String.format("""
                INSERT IGNORE INTO %s (%s) VALUES ('%s')""", TablesNames.ADMINTOKEN, ColumnNames.ADMINTOKENCOL, adminToken);
        realizeUpdateQuery(insertQuery);
    }

    /**Show the admin token in the database
     * @throws SQLException database exception
     */
    public void showTable() throws SQLException {
        String selectQuery = String.format("SELECT * FROM %s", TablesNames.ADMINTOKEN);
        ResultSet queryResult = executeSelectQuery(selectQuery);
        if (queryResult.next()) {
            String token = queryResult.getString(ColumnNames.ADMINTOKENCOL.toString());
            Supplier<String> tokenSupplier = ()-> token;
            LoggerFactory.getLogger(AdminTokenHandler.class).info(tokenSupplier);

        }
    }
}
