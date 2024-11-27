package G2.SafeSpace.repository;

import G2.SafeSpace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@code PostRepository} interface is a Spring Data JPA repository that provides CRUD operations
 * for the {@link Post} entity. It extends {@link JpaRepository}, enabling easy access to database operations
 * for managing posts.
 *
 * <p>This repository provides standard database operations such as saving, deleting, and querying posts.</p>
 */
public interface PostRepository extends JpaRepository<Post, Integer> {}
