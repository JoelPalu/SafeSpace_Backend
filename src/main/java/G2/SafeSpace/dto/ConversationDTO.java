package G2.SafeSpace.dto;
import java.util.ArrayList;

public class ConversationDTO {

    private UserDTO withUser;
    private int messageCount;
    private ArrayList<MessageDTO> messages = new ArrayList<>();

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

    public ArrayList<MessageDTO> getMessages() {
        return messages;
    }
    public void setMessages(ArrayList<MessageDTO> messages) {
        this.messages = messages;
    }

    public void addMessage(MessageDTO message) {
        if (this.messages != null) {
            this.messages.add(message);
        }
    }

    @Override
    public String toString() {
        return "ConversationDTO{" + "withUser=" + withUser + "messageCount=" + messageCount + ", messages=" + messages + '}';
    }

}
