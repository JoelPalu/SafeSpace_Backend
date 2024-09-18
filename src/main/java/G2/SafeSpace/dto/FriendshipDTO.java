package G2.SafeSpace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FriendshipDTO {
    private int requestingUserId;
    private int receivingUserId;

    @JsonIgnore
    private String eventType;

    public FriendshipDTO(int requestingUserId,
                         int receivingUserId,
                         String eventType) {
        this.requestingUserId = requestingUserId;
        this.receivingUserId = receivingUserId;
        this.eventType = eventType;
    }

    public int getRequestingUserId() {
        return requestingUserId;
    }
    public void setRequestingUserId(int requestingUserId) {
        this.requestingUserId = requestingUserId;
    }
    public int getReceivingUserId() {
        return receivingUserId;
    }
    public void setReceivingUserId(int receivingUserId) {
        this.receivingUserId = receivingUserId;
    }
    public String getEventType() {
        return eventType;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
