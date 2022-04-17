package restaurant.content;

public enum TablesNames {
    A("101"),
    B("102"),
    C("103"),
    D("104"),
    E("105"),
    F("106"),
    G("107"),
    H("108"),
    I("109"),
    J("110"),
    K("111"),
    L("112"),
    M("201"),
    N("202"),
    O("203"),
    P("204"),
    Q("205"),
    R("206"),
    S("301"),
    T("302"),
    U("303"),
    V("304"),
    W("305"),
    X("306"),
    Y("307"),
    Z("308"),
    AA("309");

    private final String tableName;

    TablesNames(String tableNameToSet) {this.tableName = tableNameToSet;}

    public String getTableName() {return this.tableName;}
}
