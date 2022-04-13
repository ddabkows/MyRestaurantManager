package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TokenDAO extends DAO {
    private static final String TOKENCOLUMN = "token";
    private final Encrypter encrypter = new Encrypter();

    public boolean checkToken(String token) throws NoSuchAlgorithmException {
        String query = String.format("SELECT * FROM token WHERE %s = '%s'", TOKENCOLUMN, encrypter.encrypt(token));
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }
}
