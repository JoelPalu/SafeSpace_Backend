package G2.SafeSpace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LikeDTO {
    private int userID;
    private int postID;

    @JsonIgnore
    private String eventType;

    public LikeDTO(int userID, int postID, String eventType) {
        this.userID = userID;
        this.postID = postID;
        this.eventType = eventType;
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getPostID() {
        return postID;
    }
    public void setPostID(int postID) {
        this.postID = postID;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
