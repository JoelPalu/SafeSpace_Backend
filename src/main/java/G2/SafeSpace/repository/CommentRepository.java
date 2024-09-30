package G2.SafeSpace.repository;

import G2.SafeSpace.entity.Comment;
import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByUser(User user);
}
