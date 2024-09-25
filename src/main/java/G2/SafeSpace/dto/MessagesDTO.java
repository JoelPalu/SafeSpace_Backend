package G2.SafeSpace.dto;

import java.util.List;

public class MessagesDTO {
    private List<MessageDTO> sentMessages;
    private List<MessageDTO> receivedMessages;

    public MessagesDTO(List<MessageDTO> sentMessages, List<MessageDTO> receivedMessages) {
        this.sentMessages = sentMessages;
        this.receivedMessages = receivedMessages;
    }

    public List<MessageDTO> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<MessageDTO> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<MessageDTO> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<MessageDTO> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }
}