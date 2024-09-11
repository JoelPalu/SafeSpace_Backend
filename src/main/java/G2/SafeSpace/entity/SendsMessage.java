package G2.SafeSpace.entity;
import jakarta.persistence.*;

@Entity
@IdClass(SendsMessageID.class)
@Table(name = "sendsmessage")
public class SendsMessage {

    @Id
    @ManyToOne
    @JoinColumn(name = "from_userID", nullable = false, updatable = false)
    private User user1;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_userID", nullable = false, updatable = false)
    private User user2;

    @Id
    @ManyToOne
    @JoinColumn(name = "messageID", nullable = false, updatable = false)
    private Message message;

    public SendsMessage() {}

    public SendsMessage(User user1, User user2, Message message) {
        this.user1 = user1;
        this.user2 = user2;
        this.message = message;
    }

}