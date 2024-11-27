package G2.SafeSpace.dto;

import java.util.ArrayList;

/**
 * Data Transfer Object (DTO) for representing a conversation between the current user and another user.
 * This class includes information such as the user with whom the conversation takes place, the total message count,
 * and the list of messages exchanged in the conversation.
 */
public class ConversationDTO {

    private UserDTO withUser;
    private int messageCount;
    private ArrayList<ConversationMessage> messages = new ArrayList<>();

    /**
     * Default constructor for creating an empty ConversationDTO object.
     * This is typically used for serialization or when constructing the DTO later.
     */
    public ConversationDTO() {}

    /**
     * Gets the user that the conversation is with.
     *
     * @return the UserDTO object representing the other user in the conversation
     */
    public UserDTO getWithUser() {
        return withUser;
    }

    /**
     * Sets the user that the conversation is with.
     *
     * @param withUser the UserDTO object representing the other user to set
     */
    public void setWithUser(UserDTO withUser) {
        this.withUser = withUser;
    }

    /**
     * Gets the total number of messages in the conversation.
     *
     * @return the number of messages exchanged in the conversation
     */
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * Sets the total number of messages in the conversation.
     *
     * @param messageCount the total message count to set
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * Increments the message count by one.
     * This method is useful for tracking the number of messages as new messages are added.
     */
    public void incrementMessageCount() {
        messageCount++;
    }

    /**
     * Gets the list of messages in the conversation.
     *
     * @return the list of ConversationMessage objects
     */
    public ArrayList<ConversationMessage> getMessages() {
        return messages;
    }

    /**
     * Sets the list of messages in the conversation.
     *
     * @param messages the list of ConversationMessage objects to set
     */
    public void setMessages(ArrayList<ConversationMessage> messages) {
        this.messages = messages;
    }

    /**
     * Adds a new message to the conversation.
     *
     * @param message the ConversationMessage object to add to the conversation
     */
    public void addMessage(ConversationMessage message) {
        if (this.messages != null) {
            this.messages.add(message);
        }
    }

    /**
     * Returns a string representation of the ConversationDTO object.
     * This can be useful for debugging purposes.
     *
     * @return a string representation of the ConversationDTO object
     */
    @Override
    public String toString() {
        return "ConversationDTO{" +
                "withUser=" + withUser +
                ", messageCount=" + messageCount +
                ", messages=" + messages +
                '}';
    }
}
