package G2.SafeSpace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Data Transfer Object (DTO) representing a "like" action on a post.
 * This DTO is used to transfer information about a "like" event, including the user who liked the post,
 * the post that was liked, and the event type (e.g., "like", "unlike").
 */
public class LikeDTO {

    private int userID;
    private int postID;

    @JsonIgnore
    private String eventType;

    /**
     * Constructs a LikeDTO object with the given user ID, post ID, and event type.
     *
     * @param userID the ID of the user who liked the post
     * @param postID the ID of the post that was liked
     * @param eventType the type of event (e.g., "like", "unlike")
     */
    public LikeDTO(int userID, int postID, String eventType) {
        this.userID = userID;
        this.postID = postID;
        this.eventType = eventType;
    }

    /**
     * Gets the user ID who liked the post.
     *
     * @return the user ID of the liker
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID who liked the post.
     *
     * @param userID the user ID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the post ID that was liked.
     *
     * @return the post ID that was liked
     */
    public int getPostID() {
        return postID;
    }

    /**
     * Sets the post ID that was liked.
     *
     * @param postID the post ID to set
     */
    public void setPostID(int postID) {
        this.postID = postID;
    }

    /**
     * Gets the event type for the "like" action (e.g., "like", "unlike").
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the event type for the "like" action.
     *
     * @param eventType the event type to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
