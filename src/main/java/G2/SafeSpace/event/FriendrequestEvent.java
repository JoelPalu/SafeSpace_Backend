package G2.SafeSpace.event;

import G2.SafeSpace.dto.FriendshipDTO;

public class FriendrequestEvent {
    private final FriendshipDTO friendshipDTO;

    public FriendrequestEvent(FriendshipDTO friendshipDTO) {
        this.friendshipDTO = friendshipDTO;
    }
    public FriendshipDTO getFriendshipDTO() {
        return friendshipDTO;
    }
}
