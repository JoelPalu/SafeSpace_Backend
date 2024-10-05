package G2.SafeSpace.dto;

import G2.SafeSpace.entity.SendsMessage;

public class ConversationMessage {
    private String Type;
    private String Message;
    private String Date;

    public ConversationMessage(SendsMessage sendsMessage, boolean type) {
        Type = type ? "sent" : "received";
        Message = sendsMessage.getMessage().getMessageContent();
        Date = sendsMessage.getMessage().getDateOfMessage();
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
