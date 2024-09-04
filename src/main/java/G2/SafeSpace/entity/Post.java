package G2.SafeSpace.entity;

import jakarta.persistence.*;

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

    public Post() {}

    public int getPostID() {
        return postID;
    }

    public String getPost_content() {
        return Post_content;
    }

    public String getPost_pictureID() {
        return Post_pictureID;
    }

    public String getPost_date() {
        return Post_date;
    }

    public void setPost_content(String post_content) {
        this.Post_content = post_content;
    }

    public void setPost_pictureID(String post_pictureID) {
        this.Post_pictureID = post_pictureID;
    }

    public void getPostDetails() {
        System.out.println("POST DETAILS:" + "\nID: " + postID + "\nCONTENT: " + Post_content + "\nPICTURE ID: " + Post_pictureID + "\nDATE: " + Post_date);
    }
}
