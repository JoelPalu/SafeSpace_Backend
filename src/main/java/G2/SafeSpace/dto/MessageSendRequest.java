package G2.SafeSpace.dto;

public class MessageSendRequest {
    private int senderID;
    private int receiverID;
    private String message;

    public int getSenderID() {
        return this.senderID;
    }

    public int getReceiverID() {
        return this.receiverID;
    }

    public String getMessage() {
        return this.message;
    }
}