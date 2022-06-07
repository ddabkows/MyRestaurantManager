package packettypes;

public enum ClosedTablesColumns {
    TYPE {public final String toString() {return "ClosedTablesPacket";}},
    ANSWERTYPE {public final String toString() {return "ClosedTablesPacketAnswer";}},

    DAY {public final String toString() {return "Day";}},

    REQUEST {public final String toString() {return "Request";}},
    ALLTABLES {public final String toString() {return "AllTables";}},
    TABLE {public final String toString() {return "Table";}},
    ANSWER {public final String toString() {return "Answer";}},
}
