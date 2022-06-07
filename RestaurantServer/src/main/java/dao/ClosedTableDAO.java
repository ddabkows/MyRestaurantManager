package dao;

import packetcomponents.ClosedTable;
import restaurant.Product;
import restaurant.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClosedTableDAO extends DAO {
    private static final String PRODUCTTABLE = "products";
    private static final String CTPTABLE = "closed_table_products";
    private static final String CLOSEDTABLESTABLE = "closed_tables";

    private static final String TABLENAMECOLUMN = "name";
    private static final String CLOSEDATCOLUMN = "closed_at";
    private static final String PRODUCTCOLUMN = "product";
    private static final String TABLEIDCOLUMN = "table_id";

    public List<ClosedTable> getByDay(String dayString) {
        String query = String.format("""
                SELECT * FROM %s
                WHERE %s LIKE '%s%s'
                ORDER BY %s
                """, CLOSEDTABLESTABLE, CLOSEDATCOLUMN, dayString, "%", CLOSEDATCOLUMN);
        try (ResultSet resultSet = realizeExecuteQuery(query)) {
            List<ClosedTable> closedTables = new ArrayList<>();
            while (resultSet.next()) {
                closedTables.add(new ClosedTable(resultSet.getString(TABLENAMECOLUMN), resultSet.getString(CLOSEDATCOLUMN)));
            }
            return closedTables;
        } catch (SQLException ignored) {
            return null;
        }
    }

    public int getTableID(String tableName, String closedAt) {
        String query = String.format("""
                SELECT * FROM %s
                WHERE %s = '%s' AND %s = '%s'
                """, CLOSEDTABLESTABLE, TABLENAMECOLUMN, tableName, CLOSEDATCOLUMN, closedAt);
        int id;
        try (ResultSet resultSet = realizeExecuteQuery(query)) {
            resultSet.next();
            id = resultSet.getInt("id");
        } catch (SQLException ignored) {
            id = -1;
        }
        return id;
    }

    public void storeList(List<Product> products, String tableName, String closedAt) {
        for (Product product : products) {
            int productID = new ProductDAO().getID(product.getName());
            int tableID = getTableID(tableName, closedAt);
            String productsQuery = String.format("""
                    INSERT INTO %s (%s, %s)
                    VALUES (%s, %s)
                    """, CTPTABLE, PRODUCTCOLUMN, TABLEIDCOLUMN, productID, tableID);
            System.out.println(productsQuery);
            realizeUpdateQuery(productsQuery);
        }
    }

    public void storeTable(Table table, String tableName, String closedAt) {
        String query = String.format("""
            INSERT INTO %s (%s, %s)
            VALUES ('%s', '%s')
            """, CLOSEDTABLESTABLE, TABLENAMECOLUMN, CLOSEDATCOLUMN, tableName, closedAt);
        realizeUpdateQuery(query);
        storeList(table.getStarters(), tableName, closedAt);
        storeList(table.getDrinks(), tableName, closedAt);
        storeList(table.getDishes(), tableName, closedAt);
        storeList(table.getDesserts(), tableName, closedAt);

    }
}
