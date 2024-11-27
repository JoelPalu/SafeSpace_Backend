package G2.SafeSpace.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a collection of messages for a user.
 * This DTO includes both sent and received messages as lists of {@link MessageDTO} objects.
 */
public class MessagesDTO {

    private List<MessageDTO> sentMessages;
    private List<MessageDTO> receivedMessages;

    /**
     * Constructs a MessagesDTO object with lists of sent and received messages.
     *
     * @param sentMessages the list of messages that were sent by the user
     * @param receivedMessages the list of messages that were received by the user
     */
    public MessagesDTO(List<MessageDTO> sentMessages, List<MessageDTO> receivedMessages) {
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
    }

    /**
     * Gets the list of sent messages.
     *
     * @return the list of sent messages
     */
    public List<MessageDTO> getSentMessages() {
        return sentMessages;
    }

    /**
     * Sets the list of sent messages.
     *
     * @param sentMessages the list of sent messages to set
     */
    public void setSentMessages(List<MessageDTO> sentMessages) {
        this.sentMessages = sentMessages;
    }

    /**
     * Gets the list of received messages.
     *
     * @return the list of received messages
     */
    public List<MessageDTO> getReceivedMessages() {
        return receivedMessages;
    }

    /**
     * Sets the list of received messages.
     *
     * @param receivedMessages the list of received messages to set
     */
    public void setReceivedMessages(List<MessageDTO> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
}