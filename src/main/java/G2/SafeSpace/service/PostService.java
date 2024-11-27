package G2.SafeSpace.service;

import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.entity.Comment;
import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.event.PostCreatedEvent;
import G2.SafeSpace.repository.CommentRepository;
import G2.SafeSpace.repository.PostRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing posts and their associated functionality.
 * This class provides methods to create, update, delete posts, handle post ownership,
 * associate users with posts, and manage comments on posts.
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CommentRepository commentRepository;

    /**
     * Constructs a PostService with the provided repositories and event publisher.
     *
     * @param postRepository    Repository for managing Post entities.
     * @param commentRepository Repository for managing Comment entities.
     * @param eventPublisher    Publisher for PostCreatedEvent.
     */
    public PostService(PostRepository postRepository,
                       CommentRepository commentRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.postRepository = postRepository;
        this.eventPublisher = eventPublisher;
        this.commentRepository = commentRepository;
    }

    /**
     * Creates a new post and associates it with a user. The post is saved to the database
     * and a PostCreatedEvent is published.
     *
     * @param post  The Post object to create.
     * @param user  The user creating the post.
     * @return      The created Post object, or null if the content is invalid.
     */
    public Post createPost(Post post, User user) {
        if (post.getPost_content() != null || post.getPost_pictureID() != null) {
            post.setPost_content(post.getPost_content().trim());
            post.setPost_pictureID(post.getPost_pictureID());
            user.addPost(post);
            Post createdPost = postRepository.save(post);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = now.format(formatter);
            PostDTO postDTO = new PostDTO(createdPost);
            postDTO.setPostDate(formattedDate);
            postDTO.setPostCreatorID(user.getUserID());
            postDTO.setPostCreatorName(user.getUsername());
            eventPublisher.publishEvent(new PostCreatedEvent(this, postDTO));
            return createdPost;
        }
        return null;
    }

    /**
     * Updates an existing post with the provided content and picture ID.
     *
     * @param id          The ID of the post to update.
     * @param updatedPost The updated Post object.
     * @return            The updated Post object, or null if not found or invalid content.
     */
    public Post updatePost(int id, Post updatedPost) {
        try {
            Optional<Post> currentPostOptional = postRepository.findById(id);
            if (currentPostOptional.isPresent()) {
                String postContent = updatedPost.getPost_content();
                String postPictureID = updatedPost.getPost_pictureID();

                if (postContent == null && postPictureID == null) {
                    return null;
                }

                Post existingPost = currentPostOptional.get();

                if (postContent != null) {
                    existingPost.setPost_content(postContent.trim());
                }
                if (postPictureID != null) {
                    existingPost.setPost_pictureID(postPictureID);
                }

                return postRepository.save(existingPost);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Post update failed, " + e.getMessage());
        }
    }

    /**
     * Finds a post by its ID.
     *
     * @param id The ID of the post to find.
     * @return   The found Post object, or null if not found.
     */
    public Post findPostById(int id) {
        try {
            Optional<Post> currentPostOptional = postRepository.findById(id);
            return currentPostOptional.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("No posts found with id " + id + " " + e.getMessage());
        }
    }

    /**
     * Associates a user with a post and sets relevant post information in the provided PostDTO.
     *
     * @param post    The Post object to associate.
     * @param postDTO The PostDTO object to set user-related information.
     */
    public void associateUserToPost(Post post, PostDTO postDTO) {
        if (!post.getUsers().isEmpty()) {
            User postCreator = post.getUsers().iterator().next();
            postDTO.setPostCreatorID(postCreator.getUserID());
            postDTO.setPostCreatorName(postCreator.getUsername());
        }

        if (!post.getLikedUsers().isEmpty()) {
            postDTO.setLikeCount(post.getLikedUsers().size());
            postDTO.setCommentCount(post.getComments().size());
        }
    }

    /**
     * Retrieves all posts from the repository, converts them into PostDTO objects,
     * and associates each post with relevant user information.
     *
     * @return A list of PostDTO objects representing all posts.
     */
    @Transactional(readOnly = true)
    public List<PostDTO> findAllPosts() {
        try {
            List<Post> rawPosts = postRepository.findAll();
            List<PostDTO> postDTOS = new ArrayList<>();
            for (Post rawPost : rawPosts) {
                PostDTO postDTO = new PostDTO(rawPost);
                associateUserToPost(rawPost, postDTO);
                postDTOS.add(postDTO);
            }
            return postDTOS;
        } catch (Exception e) {
            throw new RuntimeException("No posts found " + e.getMessage());
        }
    }

    /**
     * Deletes a post by its ID.
     *
     * @param id The ID of the post to delete.
     * @return   True if the post was deleted successfully, false otherwise.
     */
    public boolean deletePost(int id) {
        try {
            Optional<Post> optionalPost = postRepository.findById(id);
            if (optionalPost.isPresent()) {
                Post post = optionalPost.get();
                postRepository.delete(post);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error trying to delete post " + e.getMessage());
        }
    }

    /**
     * Retrieves the comments associated with a post.
     *
     * @param post The Post object for which to retrieve comments.
     * @return     A list of Comment objects associated with the post.
     */
    public List<Comment> getPostComments(Post post) {
        return new ArrayList<>(post.getComments());
    }

    /**
     * Checks if the specified user is the owner of the given post.
     *
     * @param post The Post object to check ownership of.
     * @param user The User object to check ownership against.
     * @return     True if the user is the owner of the post, false otherwise.
     */
    public boolean isPostOwner(Post post, User user) {
        return user.getPosts().contains(post);
    }

    /**
     * Checks if the specified user has already liked the given post.
     *
     * @param id    The ID of the post to check.
     * @param user  The User object to check for liking the post.
     * @return      True if the user has already liked the post, false otherwise.
     */
    public boolean alreadyLikedPost(int id, User user) {
        return user.getLikedPosts().contains(findPostById(id));
    }

    /**
     * Creates a comment on a post, associates it with the user, and saves it.
     *
     * @param comment The Comment object to create.
     * @param user    The user creating the comment.
     * @param post    The post to associate the comment with.
     * @return        The created Comment object, or null if content is invalid.
     */
    public Comment createComment(Comment comment, User user, Post post) {
        if (comment.getCommentContent() != null) {
            comment.setUser(user);
            commentRepository.save(comment);
            post.addComment(comment);
            postRepository.save(post);
            return comment;
        }
        return null;
    }

    /**
     * Finds a comment by its ID.
     *
     * @param commentID The ID of the comment to find.
     * @return          The found Comment object, or null if not found.
     */
    public Comment findCommentById(int commentID) {
        try {
            Optional<Comment> currentCommentOptional = commentRepository.findById(commentID);
            return currentCommentOptional.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("No comments found with id " + commentID + " " + e.getMessage());
        }
    }

    /**
     * Deletes a comment by its ID.
     * <p>
     * This method attempts to find the comment with the given ID in the repository.
     * If the comment is found, it is deleted and the method returns true.
     * If no comment is found with the given ID, the method returns false.
     * </p>
     *
     * @param commentID The ID of the comment to delete.
     * @return          True if the comment was successfully deleted, false if no comment with the given ID was found.
     * @throws RuntimeException If an error occurs while deleting the comment.
     */
    public boolean deleteComment(int commentID) {
        try {
            Optional<Comment> optionalComment = commentRepository.findById(commentID);
            if (optionalComment.isPresent()) {
                Comment comment = optionalComment.get();
                commentRepository.delete(comment);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error trying to delete comment " + e.getMessage());
        }
    }

    /**
     * Updates the content of an existing comment identified by its ID.
     * <p>
     * This method attempts to find the comment with the given ID in the repository.
     * If the comment is found, its content is updated with the new content provided
     * in the updatedComment object, and the updated comment is saved to the repository.
     * </p>
     *
     * @param commentID     The ID of the comment to update.
     * @param updatedComment The Comment object containing the updated content.
     * @throws RuntimeException If an error occurs while updating the comment.
     */
    public void updateComment(int commentID, Comment updatedComment) {
        try {
            Optional<Comment> currentCommentOptional = commentRepository.findById(commentID);
            if (currentCommentOptional.isPresent()) {
                Comment existingComment = currentCommentOptional.get();
                existingComment.setCommentContent(updatedComment.getCommentContent());
                commentRepository.save(existingComment);
            }
        } catch (Exception e) {
            throw new RuntimeException("Comment update failed, " + e.getMessage());
        }
    }
}
