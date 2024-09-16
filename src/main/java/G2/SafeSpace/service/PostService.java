package G2.SafeSpace.service;

import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.entity.Comment;
import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.CommentRepository;
import G2.SafeSpace.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
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

    public void associateUserToPost(Post post, PostDTO postDTO) {
        if (!post.getUsers().isEmpty()) {
            User postCreator = post.getUsers().iterator().next();
            postDTO.setPostCreatorID(postCreator.getUserID());
        }

        if (!post.getLikedUsers().isEmpty()) {
            List<Integer> likedUserIDs = new ArrayList<>();
            post.getLikedUsers().forEach(liker -> likedUserIDs.add(liker.getUserID()));
            postDTO.setLikers(likedUserIDs);
        }
    }

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

    public List<Comment> getPostComments(Post post) {
        return new ArrayList<>(post.getComments());
    }

    // check if the post is owned by the user
    public boolean isPostOwner(Post post, User user) {
        return user.getPosts().contains(post);
    }

    public boolean alreadyLikedPost(int id, User user) {
        return user.getLikedPosts().contains(findPostById(id));
    }

    public Comment createComment(Comment comment, User user, Post post)
    {
        if (comment.getCommentContent() != null)
        {
            comment.setUser(user);
            commentRepository.save(comment);
            post.addComment(comment);
            postRepository.save(post);
            return comment;
        }
        return null;
    }
}
