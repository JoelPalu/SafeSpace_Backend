package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) representing a user.
 * It contains information about the user, such as their ID, username, bio,
 * profile picture, creation date, and optionally includes their posts, liked posts,
 * and friends.
 * <p>
 * This DTO is used to transfer user-related data to the client, with optional inclusion
 * of lists like posts, liked posts, and friends, depending on the `includeLists` parameter.
 * The JWT (JSON Web Token) is included only when the user is updated (for example, when
 * the username is changed), as the token needs to be regenerated in such cases.
 * </p>
 *
 * <p>
 * Fields are annotated with {@link JsonInclude} to ensure that null fields are excluded
 * from the response, reducing the payload size.
 * </p>
 */
public class UserDTO {

    private int id;
    private String username;
    private String bio;
    private String profilePictureID;
    private String dateOfCreation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserData userData;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String jwt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> posts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> likedPosts;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> friends;

    /**
     * Constructs a UserDTO from a User entity.
     * <p>
     * The constructor initializes the DTO fields using data from the User entity and
     * optionally includes the lists of posts, liked posts, and friends based on the
     * `includeLists` parameter.
     * </p>
     *
     * @param user the User entity to initialize the DTO from
     * @param includeLists whether to include lists of posts, liked posts, and friends
     */
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

    /**
     * Gets the additional user data (e.g., following, followers, etc.).
     *
     * @return the user data as a UserData object
     */
    public UserData getUserData() {
        return userData;
    }

    /**
     * Sets the additional user data.
     *
     * @param userData the user data to set
     */
    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username as a string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the bio of the user.
     *
     * @return the bio as a string
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the bio of the user.
     *
     * @param bio the bio to set
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the profile picture ID of the user.
     *
     * @return the profile picture ID as a string
     */
    public String getProfilePictureID() {
        return profilePictureID;
    }

    /**
     * Sets the profile picture ID of the user.
     *
     * @param profilePictureID the profile picture ID to set
     */
    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID as an integer
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param id the user ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the date of creation of the user's account.
     *
     * @return the date of creation as a string
     */
    public String getDateOfCreation() {
        return this.dateOfCreation;
    }

    /**
     * Sets the date of creation of the user's account.
     *
     * @param dateOfCreation the date of creation to set
     */
    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * Gets the JWT (JSON Web Token) associated with the user.
     *
     * @return the JWT as a string
     */
    public String getJwt() {
        return jwt;
    }

    /**
     * Sets the JWT (JSON Web Token) for the user.
     *
     * @param jwt the JWT to set
     */
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    /**
     * Gets the list of post IDs associated with the user.
     *
     * @return the list of post IDs as integers
     */
    public List<Integer> getPosts() {
        return this.posts;
    }

    /**
     * Sets the list of post IDs associated with the user.
     *
     * @param posts the list of post IDs to set
     */
    public void setPosts(List<Integer> posts) {
        this.posts = posts;
    }

    /**
     * Gets the list of liked post IDs associated with the user.
     *
     * @return the list of liked post IDs as integers
     */
    public List<Integer> getLikedPosts() {
        return this.likedPosts;
    }

    /**
     * Sets the list of liked post IDs associated with the user.
     *
     * @param likedPosts the list of liked post IDs to set
     */
    public void setLikedPosts(List<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }

    /**
     * Gets the list of friend IDs associated with the user.
     *
     * @return the list of friend IDs as integers
     */
    public List<Integer> getFriends() {
        return this.friends;
    }

    /**
     * Sets the list of friend IDs associated with the user.
     *
     * @param friends the list of friend IDs to set
     */
    public void setFriends(List<Integer> friends) {
        this.friends = friends;
    }

    /**
     * Returns a string representation of the UserDTO.
     *
     * @return a string representing the UserDTO object
     */
    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", profilePictureID='" + profilePictureID + '\'' +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", jwt='" + jwt + '\'' +
                ", posts=" + posts +
                ", likedPosts=" + likedPosts +
                ", friends=" + friends +
                '}';
    }
}
