package G2.SafeSpace.entity;
import jakarta.persistence.*;

@Entity
@IdClass(SendsMessageID.class)
@Table(name = "sendsmessage")
public class SendsMessage {

    @Id
    @ManyToOne
    @JoinColumn(name = "from_userID", nullable = false, updatable = false)
    private User sender;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_userID", nullable = false, updatable = false)
    private User receiver;

    @Id
    @ManyToOne
    @JoinColumn(name = "messageID", nullable = false, updatable = false)
    private Message message;

    public SendsMessage() {}

    public SendsMessage(User sender, User receiver, Message message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User user1) {
        this.sender = user1;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User user2) {
        this.receiver = user2;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SendsMessage [sender=" + sender.getUsername() + ", receiver=" + receiver.getUsername() + ", message=" + message + "]";
    }

}