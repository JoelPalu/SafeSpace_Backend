package G2.SafeSpace.entity;

import jakarta.persistence.*;

/**
 * The SendsMessage entity represents a record of a message being sent from one user to another.
 * It is a composite entity, mapping the relationship between three entities: sender (User), receiver (User), and message (Message).
 * This entity is mapped to the "sendsmessage" table in the database.
 *
 * It uses a composite primary key defined by the {@link SendsMessageID} class.
 */
@Entity
@IdClass(SendsMessageID.class)
@Table(name = "sendsmessage")
public class SendsMessage {

    /**
     * The sender of the message.
     * This is a many-to-one relationship, where a message is sent by a user (sender).
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "from_userID", nullable = false, updatable = false)
    private User sender;

    /**
     * The receiver of the message.
     * This is a many-to-one relationship, where a message is received by a user (receiver).
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "to_userID", nullable = false, updatable = false)
    private User receiver;

    /**
     * The message being sent.
     * This is a many-to-one relationship, where a message is associated with a specific message entity.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "messageID", nullable = false, updatable = false)
    private Message message;

    /**
     * Default constructor for the SendsMessage class.
     * This constructor is used to create an empty SendsMessage object.
     */
    public SendsMessage() {}

    /**
     * Constructs a new SendsMessage with the specified sender, receiver, and message.
     *
     * @param sender the user sending the message
     * @param receiver the user receiving the message
     * @param message the message being sent
     */
    public SendsMessage(User sender, User receiver, Message message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Retrieves the sender of the message.
     *
     * @return the sender (User) of the message
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the sender of the message.
     *
     * @param sender the sender (User) to set
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Retrieves the receiver of the message.
     *
     * @return the receiver (User) of the message
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver of the message.
     *
     * @param receiver the receiver (User) to set
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     * Retrieves the message associated with this SendsMessage entity.
     *
     * @return the message being sent
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Sets the message being sent.
     *
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * Returns a string representation of this SendsMessage entity.
     *
     * @return a string containing sender, receiver, and message details
     */
    @Override
    public String toString() {
        return "SendsMessage [sender=" + sender.getUsername() + ", receiver=" + receiver.getUsername() + ", message=" + message + "]";
    }
}
