package G2.SafeSpace.repository;

import G2.SafeSpace.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {}