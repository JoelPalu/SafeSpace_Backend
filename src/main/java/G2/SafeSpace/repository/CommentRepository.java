package G2.SafeSpace.repository;

import G2.SafeSpace.entity.Comment;
import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The {@code CommentRepository} interface is a Spring Data JPA repository that provides CRUD operations
 * for the {@link Comment} entity. It extends {@link JpaRepository}, allowing easy access to the database
 * for managing comments.
 *
 * <p>This repository includes custom query methods to find comments based on the associated user.</p>
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Finds all {@link Comment} entities associated with a specific {@link User}.
     *
     * @param user the {@code User} whose comments are to be retrieved
     * @return a list of {@link Comment} entities that belong to the specified user
     */
    List<Comment> findAllByUser(User user);
}
