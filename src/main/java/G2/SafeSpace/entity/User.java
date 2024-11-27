package G2.SafeSpace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a user in the system.
 * This entity is used to store user-related data, including their username, password, bio, profile picture,
 * and various relationships such as posts, liked posts, and friends.
 * Implements {@link UserDetails} for Spring Security integration.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * The unique ID of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    /**
     * The username of the user.
     */
    @Column(nullable = false)
    private String username;

    /**
     * The password of the user (ignored for JSON serialization).
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * The bio of the user.
     */
    @Column
    private String Bio;

    /**
     * The profile picture ID of the user (insertable and updatable are false).
     */
    @Column(insertable = false)
    private String ProfilePictureID;

    /**
     * The creation date of the user's account (insertable and updatable are false).
     */
    @Column(insertable = false, updatable = false)
    private String dateOfCreation;

    /**
     * The posts that the user has created.
     * This is a many-to-many relationship with the {@link Post} entity.
     */
    @ManyToMany
    @JoinTable(
            name = "posted",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "postID")
    )
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    /**
     * The posts that the user has liked.
     * This is a many-to-many relationship with the {@link Post} entity.
     */
    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "postID")
    )
    @JsonIgnore
    private Set<Post> likedPosts = new HashSet<>();

    /**
     * The user's friends.
     * This is a many-to-many relationship with the {@link User} entity.
     */
    @ManyToMany
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "User1"),
            inverseJoinColumns = @JoinColumn(name = "User2")
    )
    @JsonIgnore
    private Set<User> friends = new HashSet<>();

    /**
     * Default constructor for the User entity.
     */
    public User() {}

    /**
     * Gets the unique ID of the user.
     *
     * @return the user ID
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns true if the account is not expired, false otherwise.
     *
     * @return true if the account is not expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Returns true if the account is not locked, false otherwise.
     *
     * @return true if the account is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Returns true if the credentials are not expired, false otherwise.
     *
     * @return true if the credentials are not expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Returns true if the user is enabled, false otherwise.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /**
     * Returns the authorities granted to the user (empty in this case).
     *
     * @return the authorities granted to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Gets the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the bio of the user.
     *
     * @return the bio
     */
    public String getBio() {
        return this.Bio;
    }

    /**
     * Gets the creation date of the user's account.
     *
     * @return the creation date
     */
    public String getDateOfCreation() {
        return this.dateOfCreation;
    }

    /**
     * Gets the profile picture ID of the user.
     *
     * @return the profile picture ID
     */
    public String getProfilePictureID() {
        return this.ProfilePictureID;
    }

    /**
     * Sets the username of the user.
     *
     * @param Username the username to set
     */
    public void setUsername(String Username) {
        this.username = Username;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the bio of the user.
     *
     * @param bio the bio to set
     */
    public void setBio(String bio) {
        this.Bio = bio;
    }

    /**
     * Sets the profile picture ID of the user.
     *
     * @param profilePictureID the profile picture ID to set
     */
    public void setProfilePictureID(String profilePictureID) {
        this.ProfilePictureID = profilePictureID;
    }

    /**
     * Returns the current user instance.
     *
     * @return the current user
     */
    public User get() {
        return this;
    }

    /**
     * Gets the posts created by the user.
     *
     * @return a set of posts created by the user
     */
    public Set<Post> getPosts() {
        return this.posts;
    }

    /**
     * Adds a post to the user's list of posts.
     *
     * @param post the post to add
     */
    public void addPost(Post post) {
        if (post != null) {
            this.posts.add(post);
            post.getUsers().add(this);
        }
    }

    /**
     * Removes a post from the user's list of posts.
     *
     * @param post the post to remove
     */
    public void removePost(Post post) {
        if (post != null) {
            this.posts.remove(post);
            post.getUsers().remove(this);
        }
    }

    /**
     * Gets the posts liked by the user.
     *
     * @return a set of liked posts
     */
    public Set<Post> getLikedPosts() {
        return this.likedPosts;
    }

    /**
     * Adds a post to the user's list of liked posts.
     *
     * @param post the post to add
     */
    public void addLikedPost(Post post) {
        if (post != null) {
            this.likedPosts.add(post);
            post.getLikedUsers().add(this);
        }
    }

    /**
     * Removes a post from the user's list of liked posts.
     *
     * @param post the post to remove
     */
    public void removeLikedPost(Post post) {
        if (post != null) {
            this.likedPosts.remove(post);
            post.getLikedUsers().remove(this);
        }
    }

    /**
     * Adds a friend to the user's list of friends.
     *
     * @param user the user to add as a friend
     */
    public void addFriends(User user) {
        if (user != null) {
            this.friends.add(user);
        }
    }

    /**
     * Removes a friend from the user's list of friends.
     *
     * @param user the user to remove from friends
     */
    public void removeFriends(User user) {
        if (user != null) {
            this.friends.remove(user);
        }
    }

    /**
     * Gets the user's list of friends.
     *
     * @return a set of friends
     */
    public Set<User> getFriends() {
        return this.friends;
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of the user entity
     */
    @Override
    public String toString() {
        return "USER ENTITY: " +
                "User ID: " + userID +
                ", Username: " + username +
                ", Password: " + password +
                ", Bio: " + Bio +
                ", ProfilePictureID: " + ProfilePictureID +
                ", DateOfCreation: " + dateOfCreation +
                "\nPosts: " + posts +
                "\nLikedPosts: " + likedPosts;
    }
}
