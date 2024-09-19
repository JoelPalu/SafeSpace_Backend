package G2.SafeSpace.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postID;

    @Column
    private String Post_content;

    @Column
    private String Post_pictureID;

    @Column(insertable = false, updatable = false)
    private String Post_date;

    @ManyToMany(mappedBy = "posts")
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> likedUsers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "postComments",
            joinColumns = @JoinColumn(name = "post_ID"),
            inverseJoinColumns = @JoinColumn(name = "comment_ID")
    )
    private Set<Comment> comments = new HashSet<>();

    public Post() {}

    public int getPostID() {
        return this.postID;
    }

    public String getPost_content() {
        return this.Post_content;
    }

    public String getPost_pictureID() {
        return this.Post_pictureID;
    }

    public String getPost_date() {
        return this.Post_date;
    }

    public void setPost_content(String post_content) {
        this.Post_content = post_content;
    }

    public void setPost_pictureID(String post_pictureID) {
        this.Post_pictureID = post_pictureID;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
            user.getPosts().add(this);
        }
    }

    public void removeUser(User user) {
        if (user != null) {
            this.users.remove(user);
            user.getPosts().remove(this);
        }
    }

    public Set<User> getLikedUsers() {
        return this.likedUsers;
    }

    public void addLikedUser(User user) {
        if (user != null) {
            this.likedUsers.add(user);
            user.getLikedPosts().add(this);
        }
    }

    public void removeLikedUser(User user) {
        if (user != null) {
            this.likedUsers.remove(user);
            user.getLikedPosts().remove(this);
        }
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void addComment(Comment comment) {
        if (comment != null) {
            this.comments.add(comment);
        }
    }

    @Override
    public String toString() {
        return "POST ENTITY: " +
                "PostID: " + postID +
                ", PostContent: " + Post_content +
                ", PostPictureID: " + Post_pictureID;
    }
}
