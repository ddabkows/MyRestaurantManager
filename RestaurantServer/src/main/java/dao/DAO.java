package dao;

import org.junit.platform.commons.logging.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class DAO {
    private final Connection connection = DBConnection.INSTANCE.getConnection();

    public ResultSet realizeExecuteQuery(String query) {
        ResultSet queryResult = null;

        try {
            queryResult = connection.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            Supplier<String> error = throwables::getMessage;
            LoggerFactory.getLogger(DAO.class).error(error);
        }

        return queryResult;
    }

    public void realizeUpdateQuery(String query){
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            Supplier<String> error = throwables::getMessage;
            LoggerFactory.getLogger(DAO.class).error(error);
        }
    }
}
