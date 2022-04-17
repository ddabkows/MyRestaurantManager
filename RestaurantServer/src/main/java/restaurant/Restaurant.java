package restaurant;


import org.json.JSONObject;
import restaurant.content.TablesNames;

import java.util.HashMap;
import java.util.Map;

public class Restaurant {
    Map<String, Table> tables = new HashMap<>();

    public Restaurant() {
        for (TablesNames tableName : TablesNames.values()) {
            tables.put(tableName.getTableName(), new Table());
        }
    }

    public JSONObject getAllTablesStatus() {
        JSONObject statuses = new JSONObject();
        for(Map.Entry<String, Table> table : tables.entrySet()) {
            statuses.put(table.getKey(), table.getValue().isOpen());
        }
        return statuses;
    }
}
