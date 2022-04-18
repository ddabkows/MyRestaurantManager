package packettypes;

public enum OpenTableColumns {
    TYPE {public final String toString() {return "OpenTablePacket";}},
    ANSWERTYPE {public final String toString() {return "OpenTablePacketAnswer";}},

    TABLE{public final String toString() {return "Table";}},
    COUNT{public final String toString() {return "Count";}},

    OPENED{public final String toString() {return "Opened";}},
}
