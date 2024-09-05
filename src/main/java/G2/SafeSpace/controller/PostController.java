package G2.SafeSpace.controller;

import G2.SafeSpace.dto.PostCreationRequest;
import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.service.PostService;
import G2.SafeSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/post/{postID}/like/{userID}")
    public ResponseEntity<Post> likePost(@PathVariable int postID, @PathVariable int userID) {
        User user = userService.findUserById(userID);
        Post post = postService.findPostById(postID);
        if (post != null && user != null) {
            user.addLikedPost(post);
            userService.save(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/post/{postID}/remove/{userID}")
    public ResponseEntity<Post> removeLike(@PathVariable int postID, @PathVariable int userID) {
        Post post = postService.findPostById(postID);
        User user = userService.findUserById(userID);
        if (post != null && user != null) {
            user.removeLikedPost(post);
            userService.save(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/post")
    public ResponseEntity<Post> createPost(@RequestBody PostCreationRequest request) {
        User user = userService.findUserById(request.getUserID());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Post post = new Post();
        post.setPost_content(request.getContent());
        post.setPost_pictureID(request.getPictureID());
        user.addPost(post);
        Post createdPost = postService.createPost(post);
        if (createdPost != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
        Post post = postService.updatePost(id, updatedPost);
        if (post != null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/post")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.findAllPosts();
        if (posts != null) {
            return ResponseEntity.ok(posts);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable int id) {
        Post post = postService.findPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable int id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
