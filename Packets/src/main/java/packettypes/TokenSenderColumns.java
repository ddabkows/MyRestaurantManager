package packettypes;

public enum TokenSenderColumns {
    TYPE {public final String toString() {return "TokenPacket";}},
    ANSWERTYPE {public final String toString() {return "TokenPacketAnswer";}},

    TOKENCOL {public final String toString() {return "Token";}},
    ANSWERCOL {public final String toString() {return "Authorize";}},
}
