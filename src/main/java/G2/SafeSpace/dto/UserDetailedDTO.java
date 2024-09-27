package G2.SafeSpace.dto;

import java.util.List;

public class UserDetailedDTO {

    private UserDTO user;

    private List<PostDTO> posts;
    private List<PostDTO> likedPosts;
    private List<UserDTO> friends;
    private MessagesDTO messages;

    public UserDetailedDTO() {}

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

    public List<UserDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<UserDTO> friends) {
        this.friends = friends;
    }

    public MessagesDTO getMessages() {
        return messages;
    }

    public void setMessages(MessagesDTO messages) {
        this.messages = messages;
    }


    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

}
