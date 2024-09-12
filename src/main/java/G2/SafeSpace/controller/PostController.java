package G2.SafeSpace.controller;

import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import G2.SafeSpace.service.PostService;
import G2.SafeSpace.service.UserContextService;
import G2.SafeSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    @Autowired
    public PostController(PostService postService,
                          UserRepository userRepository,
                          UserContextService userContextService) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.userContextService = userContextService;
    }

    //REMOVED PATH VARIABLE FROM LIKEPOST
    @PostMapping("/post/{postID}/like")
    public ResponseEntity<Post> likePost(@PathVariable int postID) {
        Optional<User> optionalUser = userContextService.getCurrentUser();
        Post post = postService.findPostById(postID);
        if (post != null && optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.addLikedPost(post);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // REMOVE PATHVARIABLE FROM REMOVELIKE
    @PostMapping("/post/{postID}/remove")
    public ResponseEntity<Post> removeLike(@PathVariable int postID) {
        Optional<User> optionalUser = userContextService.getCurrentUser();
        Post post = postService.findPostById(postID);
        if (post != null && optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.removeLikedPost(post);
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //REMOVE CUSTOM DTO CONTAINING USERID (POSTMAN POST_CONTENT, POST_PICTUREID)
    @PostMapping("/post")
    public ResponseEntity<Post> createPost(@RequestBody Post newPost) {
        Optional<User> userOptional = userContextService.getCurrentUser();
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Post createdPost = postService.createPost(newPost, userOptional.get());
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
