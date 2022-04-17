package packettypes;

public enum AllTablesColumns {
    TYPE {public final String toString() {return "AllTablesPacket";}},
    ANSWERTYPE {public final String toString() {return "AllTablesPacketAnswer";}},

    REQUEST {public final String toString() {return "Request";}},

    FETCH {public final String toString() {return "Fetch";}},
    STATUS {public final String toString() {return "Status";}}
}
