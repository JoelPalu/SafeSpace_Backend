package G2.SafeSpace.controller;

import G2.SafeSpace.dto.CommentDTO;
import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.entity.Comment;
import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.PostRepository;
import G2.SafeSpace.service.PostService;
import G2.SafeSpace.service.UserContextService;
import G2.SafeSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing posts, likes, and comments in the SafeSpace application.
 * Provides endpoints for creating, updating, deleting, and retrieving posts and comments.
 */
@RestController
@RequestMapping("api/v1")
public class PostController {
    private final PostService postService;
    private final UserContextService userContextService;
    private final PostRepository postRepository;
    private final UserService userService;

    /**
     * Constructor to initialize PostController with required services and repositories.
     *
     * @param postService        Service for handling post-related operations.
     * @param userContextService Service to retrieve the current authenticated user.
     * @param postRepository     Repository for accessing Post entities.
     * @param userService        Service for managing user-related actions.
     */
    @Autowired
    public PostController(PostService postService,
                          UserContextService userContextService,
                          PostRepository postRepository,
                          UserService userService) {
        this.postService = postService;
        this.userContextService = userContextService;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    /**
     * Helper method to get the currently authenticated user.
     *
     * @return an {@link Optional} containing the {@link User} if authenticated, otherwise empty
     */
    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }


    /**
     * Likes a post.
     *
     * @param postID ID of the post to like.
     * @return ResponseEntity with the status of the like operation.
     */
    @PostMapping("/post/{postID}/like")
    public ResponseEntity<String> likePost(@PathVariable int postID) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = postService.findPostById(postID);
        if (post != null) {
            if (postService.alreadyLikedPost(postID, optionalUser.get())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already liked this post");
            }
            if (userService.likeAdded(optionalUser.get(), post)) {
                return ResponseEntity.ok().body("Liked successfully");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add like");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
    }

    /**
     * Removes a like from a post.
     *
     * @param postID ID of the post to remove the like from.
     * @return ResponseEntity with the status of the removal operation.
     */
    @PostMapping("/post/{postID}/remove")
    public ResponseEntity<String> removeLike(@PathVariable int postID) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = postService.findPostById(postID);
        if (post != null) {
            if (!postService.alreadyLikedPost(postID, optionalUser.get())) {
                return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                        .body("Cannot remove like that was not given in the first place");
            }
            if (userService.likeRemoved(optionalUser.get(), post)) {
                return ResponseEntity.ok().body("Removed like successfully");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove like");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
    }

    /**
     * Creates a new post.
     *
     * @param newPost Post object containing the post details.
     * @return ResponseEntity with the status of the post creation.
     */
    @PostMapping("/post")
    public ResponseEntity<String> createPost(@RequestBody Post newPost) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post createdPost = postService.createPost(newPost, userOptional.get());
        if (createdPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post creation failed");
    }

    /**
     * Updates an existing post.
     *
     * @param id          ID of the post to update.
     * @param updatedPost Post object with updated details.
     * @return ResponseEntity with the status of the update operation.
     */
    @PutMapping("/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        if (!postService.isPostOwner(postOptional.get(), userOptional.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to update this post");
        }
        Post post = postService.updatePost(id, updatedPost);
        if (post != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Post updated successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post update failed");
    }

    /**
     * Retrieves all posts.
     *
     * @return ResponseEntity containing a list of PostDTOs.
     */
    @GetMapping("/post")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<PostDTO> posts = postService.findAllPosts();
        if (!posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Retrieves a specific post by its ID.
     *
     * @param id ID of the post to retrieve.
     * @return ResponseEntity containing the PostDTO.
     */
    @GetMapping("/post/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable int id) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = postService.findPostById(id);
        if (post != null) {
            PostDTO postDTO = new PostDTO(post);
            postService.associateUserToPost(post, postDTO);
            return ResponseEntity.ok(postDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a post by its ID.
     *
     * @param id ID of the post to delete.
     * @return ResponseEntity with the status of the delete operation.
     */
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        if (!postService.isPostOwner(postOptional.get(), userOptional.get())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to delete this post");
        }
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.ok().body("Post deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete post due to an internal error");
    }

    /**
     * Retrieves comments for a specific post.
     *
     * @param id ID of the post to retrieve comments for.
     * @return ResponseEntity containing a list of CommentDTOs.
     */
    @GetMapping("/post/{id}/comment")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable int id) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = postService.findPostById(id);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        if (post != null) {
            List<Comment> comments = postService.getPostComments(post);
            for (Comment comment : comments) {
                commentDTOS.add(new CommentDTO(comment));
            }
            return ResponseEntity.ok(commentDTOS);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Creates a new comment on a specific post.
     *
     * @param id      ID of the post to comment on.
     * @param comment Comment object containing the comment details.
     * @return ResponseEntity containing the created CommentDTO.
     */
    @PostMapping("/post/{id}/comment")
    public ResponseEntity<CommentDTO> createComment(@PathVariable int id, @RequestBody Comment comment) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Post post = postService.findPostById(id);
        if (post != null) {
            Comment createdComment = postService.createComment(comment, userOptional.get(), post);
            if (createdComment != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new CommentDTO(createdComment));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentID ID of the comment to delete.
     * @return ResponseEntity with the status of the delete operation.
     */
    @DeleteMapping("/post/comment/{commentID}")
    public ResponseEntity<String> deleteComment(@PathVariable int commentID) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment comment = postService.findCommentById(commentID);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }

        if (userOptional.get().getUserID() == comment.getUser().getUserID()) {
            postService.deleteComment(commentID);
            return ResponseEntity.ok().body("Comment deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to delete this comment");
    }

    /**
     * Updates a comment by its ID.
     *
     * @param commentID      ID of the comment to update.
     * @param updatedComment Comment object with updated details.
     * @return ResponseEntity with the status of the update operation.
     */
    @PutMapping("/post/comment/{commentID}")
    public ResponseEntity<String> updateComment(@PathVariable int commentID, @RequestBody Comment updatedComment) {
        Optional<User> userOptional = getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Comment comment = postService.findCommentById(commentID);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }
        if (userOptional.get().getUserID() == comment.getUser().getUserID()) {
            postService.updateComment(commentID, updatedComment);
            return ResponseEntity.ok().body("Comment updated successfully");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to update this comment");
    }


}
