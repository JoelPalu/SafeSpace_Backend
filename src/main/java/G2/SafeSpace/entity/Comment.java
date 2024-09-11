package G2.SafeSpace.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentID;

    @ManyToOne
    @JoinColumn(name = "postID", foreignKey = @ForeignKey(name = "comment_post_id"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userID", foreignKey = @ForeignKey(name = "comment_user_id"))
    private User user;


    @Column
    private String commentContent;

    @Column(name = "content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Comment() {}

    public int getCommentID() {
        return this.commentID;
    }

    public Post getPost() {
        return this.post;
    }

    public User getUser() {
        return this.user;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }


    public String getCommentDetails() {
        return "Comment{commentID=" + commentID + ", post=" + post + ", user=" + user + ", commentContent=" + commentContent + "}";
    }
}
