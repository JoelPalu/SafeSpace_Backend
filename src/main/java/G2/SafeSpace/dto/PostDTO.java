package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class PostDTO {
    private int postID;
    private int postCreatorID;
    private String postCreatorName;
    private String postContent;
    private String postPictureID;
    private String postDate;
    private int likeCount;
    private int commentCount;

    @JsonIgnore
    private String eventType;

    public PostDTO(Post post) {
        this.postID = post.getPostID();
        this.postContent = post.getPost_content();
        this.postPictureID = post.getPost_pictureID();
        this.postDate = post.getPost_date();
    }


    public int getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(int postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostPictureID() {
        return postPictureID;
    }

    public void setPostPictureID(String postPictureID) {
        this.postPictureID = postPictureID;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getCommentCount() {
        return this.commentCount;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setPostCreatorName(String postCreatorName) {
        this.postCreatorName = postCreatorName;
    }
    public String getPostCreatorName() {
        return postCreatorName;
    }

    @Override
    public String toString() {
        return "PostDTO {" +
                "postID=" + postID +
                ", postCreatorID=" + postCreatorID +
                ", postContent='" + postContent + '\'' +
                ", postPictureID='" + postPictureID + '\'' +
                ", postDate='" + postDate + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
