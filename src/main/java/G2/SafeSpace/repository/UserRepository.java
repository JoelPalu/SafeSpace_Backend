package G2.SafeSpace.repository;

import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The {@code UserRepository} interface is a Spring Data JPA repository that provides CRUD operations
 * for the {@link User} entity. It extends {@link JpaRepository} and includes custom query methods to
 * retrieve {@link User} entities based on specific criteria.
 *
 * <p>This repository includes methods for finding users by their username and finding users who follow
 * the specified user but are not yet friends with them.</p>
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Fetches a {@link User} entity by its username.
     *
     * @param username the username of the {@link User} entity to retrieve
     * @return the {@link User} entity with the given username
     */
    User findByUsername(String username);

    /**
     * Finds a list of users who are following the specified user but are not friends with them.
     * This query joins the friends relationship and excludes users who are already friends with the
     * specified user.
     *
     * @param userId the user ID of the current user to find followers for
     * @return a list of {@link User} entities who follow the current user but are not friends with them
     */
    @Query("SELECT u FROM User u JOIN u.friends f WHERE f.userID = :userId AND u NOT IN (SELECT uf FROM User u2 JOIN u2.friends uf WHERE u2.userID = :userId)")
    List<User> findFollowers(int userId);
}
