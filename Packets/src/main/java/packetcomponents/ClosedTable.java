package packetcomponents;

public class ClosedTable {
    private final String tableName;
    private final String closedAt;

    public ClosedTable(String tableNameToSet, String closedAtToSet) {
        tableName = tableNameToSet;
        closedAt = closedAtToSet;
    }

    public String getTableName() {return tableName;}
    public String getClosedAt() {return closedAt;}
}
