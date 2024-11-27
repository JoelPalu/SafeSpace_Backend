package G2.SafeSpace.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) that holds detailed information about a user,
 * including the user's personal information, their posts, liked posts,
 * conversations, and comments.
 */
public class UserDetailedDTO {

    private UserDTO user;
    private List<PostDTO> posts;
    private List<PostDTO> likedPosts;
    private List<ConversationDTO> conversations;
    private List<CommentDTO> comments;

    /**
     * Default constructor for UserDetailedDTO.
     */
    public UserDetailedDTO() {}

    /**
     * Gets the list of comments associated with the user.
     *
     * @return the list of comments as CommentDTO objects
     */
    public List<CommentDTO> getComments() {
        return comments;
    }

    /**
     * Sets the list of comments associated with the user.
     *
     * @param comments the list of comments to set
     */
    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    /**
     * Gets the list of posts created by the user.
     *
     * @return the list of posts as PostDTO objects
     */
    public List<PostDTO> getPosts() {
        return posts;
    }

    /**
     * Sets the list of posts created by the user.
     *
     * @param posts the list of posts to set
     */
    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }

    /**
     * Gets the list of posts liked by the user.
     *
     * @return the list of liked posts as PostDTO objects
     */
    public List<PostDTO> getLikedPosts() {
        return likedPosts;
    }

    /**
     * Sets the list of posts liked by the user.
     *
     * @param likedPosts the list of liked posts to set
     */
    public void setLikedPosts(List<PostDTO> likedPosts) {
        this.likedPosts = likedPosts;
    }

    /**
     * Gets the list of conversations involving the user.
     *
     * @return the list of conversations as ConversationDTO objects
     */
    public List<ConversationDTO> getConversations() {
        return conversations;
    }

    /**
     * Sets the list of conversations involving the user.
     *
     * @param conversations the list of conversations to set
     */
    public void setConversations(List<ConversationDTO> conversations) {
        this.conversations = conversations;
    }

    /**
     * Gets the user information.
     *
     * @return the user information as a UserDTO object
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Sets the user information.
     *
     * @param user the user information to set
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }
}
