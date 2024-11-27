package G2.SafeSpace.event;

import G2.SafeSpace.dto.LikeDTO;

/**
 * The {@code LikeEvent} class represents an event triggered when a user likes or unlikes a post.
 * This class encapsulates the data transfer object (DTO) containing the details of the like action.
 */
public class LikeEvent {

    /**
     * The {@code LikeDTO} object that holds the details of the like action.
     */
    private final LikeDTO likeDTO;

    /**
     * Constructs a new {@code LikeEvent} with the specified {@code LikeDTO}.
     *
     * @param likeDTO the {@code LikeDTO} containing the details of the like action
     */
    public LikeEvent(LikeDTO likeDTO) {
        this.likeDTO = likeDTO;
    }

    /**
     * Returns the {@code LikeDTO} associated with this event.
     *
     * @return the {@code LikeDTO} containing the details of the like action
     */
    public LikeDTO getLikeDTO() {
        return likeDTO;
    }
}
