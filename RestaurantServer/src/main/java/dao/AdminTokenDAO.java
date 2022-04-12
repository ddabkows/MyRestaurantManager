package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AdminTokenDAO extends DAO {
    private static final String TOKENCOLUMN = "token";
    private final Encrypter encrypter = new Encrypter();

    public boolean checkAdminToken(String token) {
        String query = "SELECT * FROM admin_token";
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            return Objects.equals(queryResult.getString(TOKENCOLUMN), encrypter.encrypt(token));
        } catch (SQLException | NullPointerException | NoSuchAlgorithmException ignored) {
            return false;
        }
    }
}
