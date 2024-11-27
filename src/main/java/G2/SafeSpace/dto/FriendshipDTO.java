package G2.SafeSpace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Data Transfer Object (DTO) representing a friendship relationship between two users.
 * This DTO is used to transfer information about a friendship request, including the
 * requesting user, receiving user, and the event type (e.g., friend request, acceptance).
 */
public class FriendshipDTO {

    private int requestingUserId;
    private int receivingUserId;

    @JsonIgnore
    private String eventType;

    /**
     * Constructs a FriendshipDTO object with the given requesting user ID, receiving user ID,
     * and event type.
     *
     * @param requestingUserId the ID of the user requesting the friendship
     * @param receivingUserId the ID of the user receiving the friendship request
     * @param eventType the type of the event (e.g., "request", "acceptance")
     */
    public FriendshipDTO(int requestingUserId,
                         int receivingUserId,
                         String eventType) {
        this.requestingUserId = requestingUserId;
        this.receivingUserId = receivingUserId;
        this.eventType = eventType;
    }

    /**
     * Gets the requesting user ID.
     *
     * @return the requesting user's ID
     */
    public int getRequestingUserId() {
        return requestingUserId;
    }

    /**
     * Sets the requesting user ID.
     *
     * @param requestingUserId the requesting user's ID to set
     */
    public void setRequestingUserId(int requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    /**
     * Gets the receiving user ID.
     *
     * @return the receiving user's ID
     */
    public int getReceivingUserId() {
        return receivingUserId;
    }

    /**
     * Sets the receiving user ID.
     *
     * @param receivingUserId the receiving user's ID to set
     */
    public void setReceivingUserId(int receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    /**
     * Gets the event type for the friendship (e.g., "request", "acceptance").
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Sets the event type for the friendship.
     *
     * @param eventType the event type to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Returns a string representation of the FriendshipDTO object, including the requesting user ID,
     * receiving user ID, and event type.
     *
     * @return a string representation of the FriendshipDTO
     */
    @Override
    public String toString() {
        return "FriendshipDTO{" +
                "requestingUserId=" + requestingUserId +
                ", receivingUserId=" + receivingUserId +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
