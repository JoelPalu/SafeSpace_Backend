package G2.SafeSpace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FriendshipDTO {
    private int requestingUserId;
    private int recievingUserId;

    @JsonIgnore
    private String eventType;

    public FriendshipDTO(int requestingUserId,
                         int recievingUserId,
                         String eventType) {
        this.requestingUserId = requestingUserId;
        this.recievingUserId = recievingUserId;
        this.eventType = eventType;
    }

    public int getRequestingUserId() {
        return requestingUserId;
    }
    public void setRequestingUserId(int requestingUserId) {
        this.requestingUserId = requestingUserId;
    }
    public int getRecievingUserId() {
        return recievingUserId;
    }
    public void setRecievingUserId(int recievingUserId) {
        this.recievingUserId = recievingUserId;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
