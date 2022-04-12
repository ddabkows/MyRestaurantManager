package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class AdminTokenDAO extends DAO {
    private static final String TOKENCOLUMN = "token";

    public boolean checkAdminToken(String token) {
        String query = "SELECT * FROM admin_token";
        try (ResultSet queryResult = realizeExecuteQuery(query)) {
            queryResult.next();
            return Objects.equals(queryResult.getString(TOKENCOLUMN), token);
        } catch (SQLException | NullPointerException ignored) {
            return false;
        }
    }
}
