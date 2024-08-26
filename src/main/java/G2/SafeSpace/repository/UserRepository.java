package G2.SafeSpace.repository;

import G2.SafeSpace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
