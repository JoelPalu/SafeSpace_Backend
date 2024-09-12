package G2.SafeSpace.service;

import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post, User user) {
        if (post.getPost_content() != null || post.getPost_pictureID() != null) {
            post.setPost_content(post.getPost_content().trim());
            post.setPost_pictureID(post.getPost_pictureID());
            user.addPost(post);
            return postRepository.save(post);
        }
        return null;
    }

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

    public Post findPostById(int id) {
        try {
            Optional<Post> currentPostOptional = postRepository.findById(id);
            return currentPostOptional.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("No posts found with id " + id + " " + e.getMessage());
        }
    }

    public List<Post> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            return null;
        }
        return posts;
    }

    //public List<Post> findPostsByUsername(String username) {}

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

    // check if the post is owned by the user
    public boolean ownPost(int id, User user) {
        return user.getPosts().contains(findPostById(id));
    }

}
