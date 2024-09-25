package G2.SafeSpace.dto;

public class MessageUpdate {
    private int messageID;
    private String message;

    public MessageUpdate(int messageID, String message) {
        this.messageID = messageID;
        this.message = message;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
    public int getMessageID() {
        return messageID;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
