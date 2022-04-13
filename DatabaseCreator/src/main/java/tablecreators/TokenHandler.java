package tablecreators;

import databaseparams.ColumnNames;
import databaseparams.TablesNames;
import org.junit.platform.commons.logging.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Supplier;

public class TokenHandler extends ConnectionHandler {
    public TokenHandler() throws NoSuchAlgorithmException, SQLException {
        String eraseQuery = String.format("DROP TABLE %s", TablesNames.TOKEN);
        realizeUpdateQuery(eraseQuery);
        createTable();
        addToken();
        showTable();
    }

    @Override
    public void createTable() {
        String createQuery = String.format("""
                CREATE TABLE IF NOT EXISTS %s (
                id int UNSIGNED AUTO_INCREMENT,
                %s VARCHAR(64) NOT NULL UNIQUE,
                PRIMARY KEY (id));
                """, TablesNames.TOKEN, ColumnNames.TOKENCOL);
        realizeUpdateQuery(createQuery);
    }

    public void addToken() throws NoSuchAlgorithmException {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add(encrypt("Kordasek35"));
        for (String token : tokens) {
            String insertQuery = String.format("INSERT IGNORE INTO %s (%s) VALUES ('%s')", TablesNames.TOKEN, ColumnNames.TOKENCOL, token);
            realizeUpdateQuery(insertQuery);
        }
    }

    public void showTable() throws SQLException {
        String selectQuery = String.format("SELECT * FROM %s", TablesNames.TOKEN);
        ResultSet queryResult = executeSelectQuery(selectQuery);
        while (queryResult.next()) {
            String token = queryResult.getString(ColumnNames.TOKENCOL.toString());
            Supplier<String> tokenSupplier = ()->token;
            LoggerFactory.getLogger(TokenHandler.class).info(tokenSupplier);
        }
    }
}
