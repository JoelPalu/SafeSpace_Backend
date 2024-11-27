package G2.SafeSpace.dto;

import G2.SafeSpace.entity.SendsMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Data Transfer Object (DTO) representing a message sent between users.
 * This DTO is used to transfer information about a message, including the sender, receiver, message content,
 * date, and the associated event type (e.g., "sent", "received").
 */
public class MessageDTO {

    private int messageID;
    private int sender;
    private int receiver;
    private String message;
    private String date;

    @JsonIgnore
    private String eventType;

    /**
     * Constructs a MessageDTO object from a SendsMessage entity.
     *
     * @param sendsMessage the SendsMessage entity containing the message data
     */
    public MessageDTO(SendsMessage sendsMessage) {
        this.messageID = sendsMessage.getMessage().getMessageID();
        this.sender = sendsMessage.getSender().getUserID();
        this.receiver = sendsMessage.getReceiver().getUserID();
        this.message = sendsMessage.getMessage().getMessageContent();
        this.date = sendsMessage.getMessage().getDateOfMessage();
    }

    /**
     * Gets the message ID.
     *
     * @return the message ID
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * Sets the message ID.
     *
     * @param messageID the message ID to set
     */
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    /**
     * Gets the sender's user ID.
     *
     * @return the sender's user ID
     */
    public int getSender() {
        return sender;
    }

    /**
     * Sets the sender's user ID.
     *
     * @param sender the sender's user ID to set
     */
    public void setSender(int sender) {
        this.sender = sender;
    }

    /**
     * Gets the receiver's user ID.
     *
     * @return the receiver's user ID
     */
    public int getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver's user ID.
     *
     * @param receiver the receiver's user ID to set
     */
    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    /**
     * Gets the message content.
     *
     * @return the message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content.
     *
     * @param message the message content to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the date the message was sent.
     *
     * @return the date of the message
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the message was sent.
     *
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the event type for the message (e.g., "sent", "received").
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the event type for the message (e.g., "sent", "received").
     *
     * @param eventType the event type to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
