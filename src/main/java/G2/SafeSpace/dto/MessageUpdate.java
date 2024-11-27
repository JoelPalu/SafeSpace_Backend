package G2.SafeSpace.dto;

/**
 * Data Transfer Object (DTO) used for updating an existing message.
 * This class contains the message ID and the new message content that will be updated.
 */
public class MessageUpdate {

    private int messageID;
    private String message;

    /**
     * Constructs a MessageUpdate object with the specified message ID and message content.
     *
     * @param messageID the ID of the message to be updated
     * @param message the new message content to replace the existing message
     */
    public MessageUpdate(int messageID, String message) {
        this.messageID = messageID;
        this.message = message;
    }

    /**
     * Gets the ID of the message to be updated.
     *
     * @return the message ID
     */
    public int getMessageID() {
        return messageID;
    }

    /**
     * Sets the ID of the message to be updated.
     *
     * @param messageID the message ID to set
     */
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    /**
     * Gets the new message content.
     *
     * @return the new message content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the new message content.
     *
     * @param message the new message content to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
