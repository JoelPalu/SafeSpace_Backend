package G2.SafeSpace.dto;

import G2.SafeSpace.entity.SendsMessage;

/**
 * Represents a message exchanged in a conversation, encapsulating the message's type (sent or received),
 * the message content, and the date the message was sent.
 */
public class ConversationMessage {

    private String Type;
    private String Message;
    private String Date;

    /**
     * Constructs a ConversationMessage object based on the provided SendsMessage entity and the type of the message.
     *
     * @param sendsMessage the SendsMessage entity containing the actual message and its details
     * @param type a boolean indicating if the message is sent (true) or received (false)
     */
    public ConversationMessage(SendsMessage sendsMessage, boolean type) {
        Type = type ? "sent" : "received";
        Message = sendsMessage.getMessage().getMessageContent();
        Date = sendsMessage.getMessage().getDateOfMessage();
    }

    /**
     * Gets the message content.
     *
     * @return the content of the message
     */
    public String getMessage() {
        return Message;
    }

    /**
     * Sets the message content.
     *
     * @param message the content of the message to set
     */
    public void setMessage(String message) {
        Message = message;
    }

    /**
     * Gets the type of the message (either "sent" or "received").
     *
     * @return the type of the message
     */
    public String getType() {
        return Type;
    }

    /**
     * Sets the type of the message (either "sent" or "received").
     *
     * @param type the type of the message to set
     */
    public void setType(String type) {
        Type = type;
    }

    /**
     * Gets the date when the message was sent.
     *
     * @return the date of the message
     */
    public String getDate() {
        return Date;
    }

    /**
     * Sets the date of the message.
     *
     * @param date the date to set for the message
     */
    public void setDate(String date) {
        Date = date;
    }
}
