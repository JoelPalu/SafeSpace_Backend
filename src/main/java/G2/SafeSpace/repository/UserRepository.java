package G2.SafeSpace.repository;

import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    //Find users that are following current user but not friends
    @Query("SELECT u FROM User u JOIN u.friends f WHERE f.userID = :userId AND u NOT IN (SELECT uf FROM User u2 JOIN u2.friends uf WHERE u2.userID = :userId)")
    List<User> findFollowers(int userId);
}

