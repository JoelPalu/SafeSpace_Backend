package G2.SafeSpace.repository;

import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.SendsMessageID;
import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SendsMessageRepository extends JpaRepository<SendsMessage, SendsMessageID> {

    // Fetch messages where the user is the sender
    List<SendsMessage> findBySender(User sender);

    // Fetch messages where the user is the receiver
    List<SendsMessage> findByReceiver(User receiver);
}
