package G2.SafeSpace.repository;

import G2.SafeSpace.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@code MessageRepository} interface is a Spring Data JPA repository that provides CRUD operations
 * for the {@link Message} entity. It extends {@link JpaRepository}, allowing for easy interaction with the
 * database to manage messages.
 *
 * <p>This repository provides standard database operations like saving, deleting, and querying messages.</p>
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {}