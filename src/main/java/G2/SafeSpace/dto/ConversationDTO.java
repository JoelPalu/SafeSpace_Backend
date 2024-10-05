package G2.SafeSpace.dto;
import java.util.ArrayList;

public class ConversationDTO {

    private UserDTO withUser;
    private int messageCount;
    private ArrayList<ConversationMessage> messages = new ArrayList<>();

    public ConversationDTO () {}

    public UserDTO getWithUser() {
        return withUser;
    }
    public void setWithUser(UserDTO withUser) {
        this.withUser = withUser;
    }
    public int getMessageCount() {
        return messageCount;
    }
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }
    public void incrementMessageCount() {
        messageCount++;
    }

    public ArrayList<ConversationMessage> getMessages() {
        return messages;
    }
    public void setMessages(ArrayList<ConversationMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ConversationMessage message) {
        if (this.messages != null) {
            this.messages.add(message);
        }
    }

    @Override
    public String toString() {
        return "ConversationDTO{" + "withUser=" + withUser + "messageCount=" + messageCount + ", messages=" + messages + '}';
    }

}
