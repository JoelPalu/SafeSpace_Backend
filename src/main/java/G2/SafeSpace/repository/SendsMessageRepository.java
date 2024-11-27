package G2.SafeSpace.repository;

import G2.SafeSpace.entity.Message;
import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.SendsMessageID;
import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The {@code SendsMessageRepository} interface is a Spring Data JPA repository that provides CRUD operations
 * for the {@link SendsMessage} entity. It extends {@link JpaRepository} and supports operations for managing
 * the relationship between {@link User} entities and {@link Message} entities through the {@link SendsMessage} entity.
 *
 * <p>This repository includes methods to find messages by sender, receiver, or both.</p>
 */
public interface SendsMessageRepository extends JpaRepository<SendsMessage, SendsMessageID> {

    /**
     * Fetches a list of {@link SendsMessage} where the provided user is the sender.
     *
     * @param sender the {@link User} who sent the messages
     * @return a list of {@link SendsMessage} instances where the user is the sender
     */
    List<SendsMessage> findBySender(User sender);

    /**
     * Fetches a list of {@link SendsMessage} where the provided user is the receiver.
     *
     * @param receiver the {@link User} who received the messages
     * @return a list of {@link SendsMessage} instances where the user is the receiver
     */
    List<SendsMessage> findByReceiver(User receiver);

    /**
     * Fetches a {@link SendsMessage} by the provided {@link Message}.
     *
     * @param message the {@link Message} entity associated with the send record
     * @return the {@link SendsMessage} associated with the given {@link Message}
     */
    SendsMessage findByMessage(Message message);

    /**
     * Fetches all {@link SendsMessage} instances where the user is either the sender or the receiver.
     *
     * @param user the {@link User} who may be either the sender or the receiver
     * @param user1 the other {@link User} who may be either the sender or the receiver
     * @return a list of {@link SendsMessage} instances where the user is involved as sender or receiver
     */
    List<SendsMessage> findBySenderOrReceiver(User user, User user1);
}
