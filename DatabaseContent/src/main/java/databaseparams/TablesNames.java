package databaseparams;

public enum TablesNames {
    ADMINTOKEN  {public final String toString() {return "admin_token";}},
    TOKEN {public final String toString() {return "token";}},
    CATEGORY {public final String toString() {return "food_type";}},
    PRODUCT {public final String toString() {return "products";}},

    CLOSEDTABLES {public final String toString() {return "closed_tables";}},

    CLOSEDTABLESPRODUCTS {public final String toString() {return "closed_table_products";}},
}
