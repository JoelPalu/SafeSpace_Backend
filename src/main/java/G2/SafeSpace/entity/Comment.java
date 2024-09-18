package G2.SafeSpace.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentID;

    @ManyToOne
    @JoinColumn(name = "userID", foreignKey = @ForeignKey(name = "comment_user_id"))
    private User user;

    @Column
    private String commentContent;


    public Comment() {}

    public int getCommentID() {
        return this.commentID;
    }

    public User getUser() {
        return this.user;
    }

    public String getCommentContent() {
        return this.commentContent;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }


    public String getCommentDetails() {
        return "Comment{commentID=" + commentID + ", user=" + user + ", commentContent=" + commentContent + "}";
    }
}
