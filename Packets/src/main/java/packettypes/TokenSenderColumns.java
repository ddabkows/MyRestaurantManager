package packettypes;

public enum TokenSenderColumns {
    TYPE {public final String toString() {return "TokenPacket";}},
    ANSWERTYPE {public final String toString() {return "TokenPacketAnswer";}},

    TOKENCOL {public final String toString() {return "Token";}},
    ANSWERCOL {public final String toString() {return "Authorize";}},
    USERTYPECOL {public final String toString() {return "UserType";}},

    ADMIN {public final String toString() {return "Admin";}},
    USER {public final String toString() {return "User";}},
}
