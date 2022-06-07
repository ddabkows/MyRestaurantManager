package databaseparams;


public enum ColumnNames {
    ADMINTOKENCOL {public final String toString() {return "token";}},
    TOKENCOL {public final String toString() {return "token";}},
    CATEGORYCOL {public final String toString() {return "category";}},
    PRODUCTNAME {public final String toString() {return "name";}},

    TABLENAME {public final String toString() {return "name";}},
    PRODUCTFK {public final String toString() {return "product";}},
    TABLEFK {public final String toString() {return "table_id";}},

    DATE {public final String toString() {return "closed_at";}},
}
