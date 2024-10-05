package G2.SafeSpace.dto;

import java.util.List;

public class UserDetailedDTO {

    private UserDTO user;
    private int followingCount;
    private int followersCount;
    private int friendsCount;
    private List<PostDTO> posts;
    private List<PostDTO> likedPosts;
    private List<UserDTO> following;
    private List<UserDTO> followers;
    private List<UserDTO> friends;
    private List<ConversationDTO> conversations;
    private List<CommentDTO> comments;

    public UserDetailedDTO() {}


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

    public List<UserDTO> getFollowing() {
        return following;
    }

    public void setFollowing(List<UserDTO> following) {
        this.following = following;
    }

    public List<UserDTO> getFollowers() {
        return followers;
    }
    public void setFollowers(List<UserDTO> followers) {
        this.followers = followers;
    }
    public List<UserDTO> getFriends() {
        return friends;
    }
    public void setFriends(List<UserDTO> friends) {
        this.friends = friends;
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

    public int getFollowingCount() {
        return followingCount;
    }
    public void addFollowingCount() {
        this.followingCount++;
    }
    public int getFollowersCount() {
        return followersCount;
    }
    public void addFollowersCount() {
        this.followersCount++;
    }
    public int getFriendsCount() {
        return friendsCount;
    }
    public void addFriendsCount() {
        this.friendsCount++;
    }

}
