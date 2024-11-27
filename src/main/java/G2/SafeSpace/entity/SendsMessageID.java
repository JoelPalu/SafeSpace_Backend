package G2.SafeSpace.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * The SendsMessageID class represents the composite primary key for the {@link SendsMessage} entity.
 * This class is used to uniquely identify each record in the "sendsmessage" table by combining the sender, receiver, and message.
 * It implements {@link Serializable} to be used as a composite key in JPA.
 */
public class SendsMessageID implements Serializable {

    /**
     * The ID of the sender (User) of the message.
     */
    private int sender;

    /**
     * The ID of the receiver (User) of the message.
     */
    private int receiver;

    /**
     * The ID of the message being sent.
     */
    private int message;

    /**
     * Default constructor for the SendsMessageID class.
     * This constructor is required for JPA and Hibernate.
     */
    public SendsMessageID() {}

    /**
     * Constructs a new SendsMessageID with the specified sender, receiver, and message.
     * This constructor is used to create a composite key for the SendsMessage entity.
     *
     * @param sender the ID of the sender (User)
     * @param receiver the ID of the receiver (User)
     * @param message the ID of the message being sent
     */
    public SendsMessageID(int sender, int receiver, int message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Checks if two SendsMessageID objects are equal based on their sender, receiver, and message.
     *
     * @param o the object to compare with this instance
     * @return true if the two objects are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendsMessageID that = (SendsMessageID) o;
        return sender == that.sender && receiver == that.receiver && message == that.message;
    }

    /**
     * Generates a hash code based on the sender, receiver, and message.
     * This method is used to ensure the uniqueness of this composite key in hash-based collections.
     *
     * @return the hash code of this SendsMessageID
     */
    @Override
    public int hashCode() {
        return Objects.hash(sender, receiver, message);
    }

    /**
     * Retrieves the ID of the receiver (User).
     *
     * @return the receiver's ID
     */
    public int getReceiver() {
        return receiver;
    }

    /**
     * Sets the ID of the receiver (User).
     *
     * @param receiver the ID of the receiver (User) to set
     */
    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    /**
     * Retrieves the ID of the sender (User).
     *
     * @return the sender's ID
     */
    public int getSender() {
        return sender;
    }

    /**
     * Sets the ID of the sender (User).
     *
     * @param sender the ID of the sender (User) to set
     */
    public void setSender(int sender) {
        this.sender = sender;
    }

    /**
     * Retrieves the ID of the message being sent.
     *
     * @return the message's ID
     */
    public int getMessage() {
        return message;
    }

    /**
     * Sets the ID of the message being sent.
     *
     * @param message the ID of the message to set
     */
    public void setMessage(int message) {
        this.message = message;
    }

}
