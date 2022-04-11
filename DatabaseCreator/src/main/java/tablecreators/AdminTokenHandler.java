package tablecreators;


import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**Method responsible for creating the admin_token table
 * and adding the admintoken
 */
public class AdminTokenHandler extends ConnectionHandler {
    private final String tokenColumn = "token";
    public AdminTokenHandler() throws NoSuchAlgorithmException, SQLException {
        createTable();
        addAdminToken();
        showTable();
    }

    /**Creates the admin_token table
     *
     */
    @Override
    public void createTable() {
        String createQuery = """
                CREATE TABLE IF NOT EXISTS admin_token (
                id int UNSIGNED AUTO_INCREMENT,
                token VARCHAR(64) NOT NULL UNIQUE,
                PRIMARY KEY (id));""";
        realizeUpdateQuery(createQuery);
    }

    /**Adds the admin token
     *
     */
    public void addAdminToken() throws NoSuchAlgorithmException {
        String adminToken = encrypt("1011str1012t1013mintok14n");
        String insertQuery = String.format("""
                INSERT INTO admin_token (%s) VALUES ('%s')""", tokenColumn, adminToken);
        realizeUpdateQuery(insertQuery);
    }

    /**Show the admin token in the database
     * @throws SQLException database exception
     */
    public void showTable() throws SQLException {
        String selectQuery = "SELECT * FROM admin_token";
        ResultSet queryResult = executeSelectQuery(selectQuery);
        if (queryResult.next()) {
            System.out.println(queryResult.getString(tokenColumn));
        }
    }
}
