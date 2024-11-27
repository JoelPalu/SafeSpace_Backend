package G2.SafeSpace.dto;

import G2.SafeSpace.entity.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) for the Post entity.
 * This class represents a post with essential details such as content, picture,
 * creator information, and counts of likes and comments.
 */
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

    /**
     * Constructs a PostDTO object using the given Post entity.
     * This constructor initializes the DTO with the basic properties of the Post.
     *
     * @param post the Post entity to initialize the DTO from
     */
    public PostDTO(Post post) {
        this.postID = post.getPostID();
        this.postContent = post.getPost_content();
        this.postPictureID = post.getPost_pictureID();
        this.postDate = post.getPost_date();
    }

    /**
     * Gets the ID of the post creator.
     *
     * @return the ID of the post creator
     */
    public int getPostCreatorID() {
        return postCreatorID;
    }

    /**
     * Sets the ID of the post creator.
     *
     * @param postCreatorID the ID of the post creator to set
     */
    public void setPostCreatorID(int postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    /**
     * Gets the ID of the post.
     *
     * @return the post ID
     */
    public int getPostID() {
        return postID;
    }

    /**
     * Sets the ID of the post.
     *
     * @param postID the post ID to set
     */
    public void setPostID(int postID) {
        this.postID = postID;
    }

    /**
     * Gets the content of the post.
     *
     * @return the post content
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     * Sets the content of the post.
     *
     * @param postContent the content of the post to set
     */
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /**
     * Gets the ID of the post picture.
     *
     * @return the post picture ID
     */
    public String getPostPictureID() {
        return postPictureID;
    }

    /**
     * Sets the ID of the post picture.
     *
     * @param postPictureID the post picture ID to set
     */
    public void setPostPictureID(String postPictureID) {
        this.postPictureID = postPictureID;
    }

    /**
     * Gets the date the post was created.
     *
     * @return the post creation date
     */
    public String getPostDate() {
        return postDate;
    }

    /**
     * Sets the date the post was created.
     *
     * @param postDate the post creation date to set
     */
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    /**
     * Gets the number of likes the post has.
     *
     * @return the number of likes
     */
    public int getLikeCount() {
        return this.likeCount;
    }

    /**
     * Sets the number of likes the post has.
     *
     * @param likeCount the number of likes to set
     */
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * Gets the number of comments the post has.
     *
     * @return the number of comments
     */
    public int getCommentCount() {
        return this.commentCount;
    }

    /**
     * Sets the number of comments the post has.
     *
     * @param commentCount the number of comments to set
     */
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * Gets the event type associated with the post.
     * This is typically used to track actions related to the post, such as updates or deletions.
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the event type associated with the post.
     *
     * @param eventType the event type to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Gets the name of the post creator.
     *
     * @return the name of the post creator
     */
    public String getPostCreatorName() {
        return postCreatorName;
    }

    /**
     * Sets the name of the post creator.
     *
     * @param postCreatorName the name of the post creator to set
     */
    public void setPostCreatorName(String postCreatorName) {
        this.postCreatorName = postCreatorName;
    }

    /**
     * Returns a string representation of the PostDTO object.
     *
     * @return a string representation of the PostDTO
     */
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
