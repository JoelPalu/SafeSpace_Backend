package G2.SafeSpace.entity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;

    @Column
    private String messageContent;

    @Column(insertable = false, updatable = false)
    private String dateOfMessage;


    public Message() {}

    public int getMessageID() {
        return this.messageID;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public String getDateOfMessage() {
        return this.dateOfMessage;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}