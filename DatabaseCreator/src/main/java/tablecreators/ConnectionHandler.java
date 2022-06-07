package tablecreators;


import org.junit.platform.commons.logging.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.function.Supplier;

import main.Main;


/**Abstract class that handles the connection
 *
 */
public abstract class ConnectionHandler {
    public Connection connection = null;

    /**The constructor sets up the connection to the database
     *
     */
    ConnectionHandler() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantmanager",
                                                     "restaurantmanager",
                                                     "1013mintok14n");
        } catch (SQLException throwables) {
            Supplier<String> errorMessage = ()-> "Connection failed";
            LoggerFactory.getLogger(Main.class).error(errorMessage);
        }
    }

    /**Method that gets a ResultSet from a Select query
     * @param query select query to be executed
     * @return a ResultSet
     */
    public ResultSet executeSelectQuery(String query) {
        ResultSet queryResult = null;
        try {
            queryResult = connection.createStatement().executeQuery(query);
        } catch (SQLException throwables) {
            Supplier<String> errorMessage = throwables::getMessage;
            LoggerFactory.getLogger(Main.class).error(errorMessage);
        }
        return queryResult;
    }

    /**Method that executes an Update, Delete or Create query
     * @param query update/delete/create query
     */
    public void realizeUpdateQuery(String query) {
        try {
            connection.createStatement().executeUpdate(query);
        } catch (SQLException throwables) {
            Supplier<String> errorMessage = throwables::getMessage;
            LoggerFactory.getLogger(Main.class).error(errorMessage);
        }
    }

    /**Method creating a table
     *
     */
    public abstract void createTable();

    public String encrypt(String stringToEncrypt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(stringToEncrypt.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte hash : encodedHash) {
            String hex = Integer.toHexString(0xff & hash);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
