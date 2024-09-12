package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    private int id;
    private String username;
    private String bio;
    private String profilePictureID;
    private String dateOfCreation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jwt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> posts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> likedPosts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> friends;

    // This userDTO is used to transfer data to the client with optional list/jwt inclusion.
    // This DTO is intended to be used across all user-related data transfers to client.

    // @JsonInclude.NON_NULL ensures fields are omitted from the response if they are null.
    // If `includeLists` is false, lists are excluded from the response (remain null).

    // `jwt` is included in the response only when the user is updated (e.g., in updateUser),
    // because the jwt needs to be regenerated when the username is changed.
    // The new jwt is necessary for authentication (JwtAuthenticationFilter.java).

    public UserDTO(User user, boolean includeLists) {
        this.id = user.getUserID();
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.profilePictureID = user.getProfilePictureID();
        this.dateOfCreation = user.getDateOfCreation();

        if (includeLists) {
            this.posts = user.getPosts().stream().map(Post::getPostID).collect(Collectors.toList());
            this.likedPosts = user.getLikedPosts().stream().map(Post::getPostID).collect(Collectors.toList());
            this.friends = user.getFriends().stream().map(User::getUserID).collect(Collectors.toList());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureID() {
        return profilePictureID;
    }

    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateOfCreation() {
        return this.dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public List<Integer> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Integer> posts) {
        this.posts = posts;
    }

    public List<Integer> getLikedPosts() {
        return this.likedPosts;
    }

    public void setLikedPosts(List<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public List<Integer> getFriends() {
        return this.friends;
    }

    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }
}
