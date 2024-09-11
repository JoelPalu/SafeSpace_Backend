package G2.SafeSpace.repository;

import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.SendsMessageID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendsMessageRepository extends JpaRepository<SendsMessage, SendsMessageID> {}
