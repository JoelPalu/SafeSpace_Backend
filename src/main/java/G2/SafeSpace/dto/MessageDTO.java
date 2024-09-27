package G2.SafeSpace.dto;

import G2.SafeSpace.entity.SendsMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class MessageDTO {
    private int messageID;
    private int sender;
    private int receiver;
    private String message;
    private String date;

    @JsonIgnore
    private String eventType;

    public MessageDTO() {}

    public MessageDTO(SendsMessage sendsMessage) {
        this.messageID = sendsMessage.getMessage().getMessageID();
        this.sender = sendsMessage.getSender().getUserID();
        this.receiver = sendsMessage.getReceiver().getUserID();
        this.message = sendsMessage.getMessage().getMessageContent();
        this.date = sendsMessage.getMessage().getDateOfMessage();
    }

    public int getMessageID() {
        return messageID;
    }
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
    public int getSender() {
        return sender;
    }
    public void setSender(int sender) {
        this.sender = sender;
    }
    public int getReceiver() {
        return receiver;
    }
    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
