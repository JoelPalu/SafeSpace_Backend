package G2.SafeSpace.entity;

import java.io.Serializable;
import java.util.Objects;

public class SendsMessageID implements Serializable {
    private int sender;
    private int receiver;
    private int message;

    public SendsMessageID() {}

    public SendsMessageID(int sender, int receiver, int message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendsMessageID that = (SendsMessageID) o;
        return sender == that.sender && receiver == that.receiver && message == that.message;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, message);
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

}
