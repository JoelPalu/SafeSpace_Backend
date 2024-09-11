package G2.SafeSpace.entity;

import java.io.Serializable;
import java.util.Objects;

public class SendsMessageID implements Serializable {
    private int fromUserID;
    private int toUserID;
    private int messageID;


    public SendsMessageID() {}

    public SendsMessageID(int fromUserID, int toUserID, int messageID) {
        this.fromUserID = fromUserID;
        this.toUserID = toUserID;
        this.messageID = messageID;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendsMessageID that = (SendsMessageID) o;
        return fromUserID == that.fromUserID && toUserID == that.toUserID && messageID == that.messageID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUserID, toUserID, messageID);
    }
}
