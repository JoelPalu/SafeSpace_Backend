package G2.SafeSpace.dto;

import java.util.List;

public class UserDetailedDTO {

    private UserDTO user;
    private UserData userData;
    private List<PostDTO> posts;
    private List<PostDTO> likedPosts;
    private List<ConversationDTO> conversations;
    private List<CommentDTO> comments;

    public UserDetailedDTO() {}


    public UserData getUserData() {
        return userData;
    }
    public void setUserData(UserData userData) {
        this.userData = userData;
    }
    public List<CommentDTO> getComments() {
        return comments;
    }
    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }

    public List<PostDTO> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<PostDTO> likedPosts) {
        this.likedPosts = likedPosts;
    }


    public List<ConversationDTO> getConversations() {
        return conversations;
    }

    public void setConversations(List<ConversationDTO> conversations) {
        this.conversations = conversations;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }


}
