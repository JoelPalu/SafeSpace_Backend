package G2.SafeSpace.event;

import G2.SafeSpace.dto.FriendshipDTO;

/**
 * The {@code FriendrequestEvent} class represents an event triggered when a user sends or receives a friendship request.
 * This class encapsulates the data transfer object (DTO) containing the details of the friendship request.
 */
public class FriendrequestEvent {

    /**
     * The {@code FriendshipDTO} object that holds the details of the friendship request.
     */
    private final FriendshipDTO friendshipDTO;

    /**
     * Constructs a new {@code FriendrequestEvent} with the specified {@code FriendshipDTO}.
     *
     * @param friendshipDTO the {@code FriendshipDTO} containing the details of the friendship request
     */
    public FriendrequestEvent(FriendshipDTO friendshipDTO) {
        this.friendshipDTO = friendshipDTO;
    }

    /**
     * Returns the {@code FriendshipDTO} associated with this event.
     *
     * @return the {@code FriendshipDTO} containing the details of the friendship request
     */
    public FriendshipDTO getFriendshipDTO() {
        return friendshipDTO;
    }
}
