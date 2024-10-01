package G2.SafeSpace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private String Bio;

    @Column(insertable = false)
    private String ProfilePictureID;

    @Column(insertable = false, updatable = false)
    private String dateOfCreation;

    @ManyToMany
    @JoinTable(
            name = "posted",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "postID")
    )
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "userID"),
            inverseJoinColumns = @JoinColumn(name = "postID")
    )
    @JsonIgnore
    private Set<Post> likedPosts = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "friendship",
            joinColumns = @JoinColumn(name = "User1"),
            inverseJoinColumns = @JoinColumn(name = "User2")
    )
    @JsonIgnore
    private Set<User> friends = new HashSet<>();

    public User() {}

    public int getUserID() {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return this.password;
    }

    public String getBio() {
        return this.Bio;
    }

    public String getDateOfCreation() {
        return this.dateOfCreation;
    }

    public String getProfilePictureID() {
        return this.ProfilePictureID;
    }

    public void setUsername(String Username) {
        this.username = Username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBio(String bio) {
        this.Bio = bio;
    }

    public void setProfilePictureID(String profilePictureID) {
        this.ProfilePictureID = profilePictureID;
    }

    public User get() {
        return this;
    }


    public Set<Post> getPosts() {
        return this.posts;
    }

    public void addPost(Post post) {
        if (post != null) {
            this.posts.add(post);
            post.getUsers().add(this);
        }
    }

    public void removePost(Post post) {
        if (post != null) {
            this.posts.remove(post);
            post.getUsers().remove(this);
        }
    }

    public Set<Post> getLikedPosts() {
        return this.likedPosts;
    }

    public void addLikedPost(Post post) {
        if (post != null) {
            this.likedPosts.add(post);
            post.getLikedUsers().add(this);
        }
    }

    public void removeLikedPost(Post post) {
        if (post != null) {
            this.likedPosts.remove(post);
            post.getLikedUsers().remove(this);
        }
    }


    public void addFriends(User user) {
        if (user != null) {
            this.friends.add(user);
        }
    }

    public void removeFriends(User user) {
        if (user != null) {
            this.friends.remove(user);
        }
    }

    public Set<User> getFriends() {
        return this.friends;
    }

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
