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

@RestController
@RequestMapping("api/v1")
public class PostController {
    private final PostService postService;
    private final UserContextService userContextService;
    private final PostRepository postRepository;
    private final UserService userService;

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

    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }


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
