package packettypes;

public enum TableValuesColumns {
    TYPE {public final String toString() {return "TableValuesPacket";}},
    ANSWERTYPE {public final String toString() {return "TableValuesPacketAnswer";}},

    TABLENAME {public final String toString() {return "TableName";}},

    REQUEST {public final String toString() {return "Request";}},

    FETCH {public final String toString() {return "Fetch";}},
    SET {public final String toString() {return "Set";}},
    PRINT {public final String toString() {return "Print";}},
    PRINTER {public final String toString() {return "Printer";}},
    TOPRINT {public final String toString() {return "ToPrint";}},
    STARTERS {public final String toString() {return "Starters";}},
    VALUES {public final String toString() {return "Values";}},
    PEOPLECOUNT {public final String toString() {return "PeopleCount";}},
    PRODUCTS {public final String toString() {return "Products";}},
    CONFIRMED {public final String toString() {return "Confirmed";}},
    PRODUCTNAME {public final String toString() {return "ProductName";}},
    PRODUCTQUANTITY {public final String toString() {return "ProductQuantity";}},
    PRODUCTTYPE {public final String toString() {return "ProductType";}},
    PRODUCTPRICE {public final String toString() {return "Price";}},
    PRODUCTCOMMENT {public final String toString() {return "Comment";}},

    CLOSE {public final String toString() {return "Close";}},
}
