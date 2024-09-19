package G2.SafeSpace.event;

import G2.SafeSpace.dto.LikeDTO;

public class LikeEvent {
    private final LikeDTO likeDTO;

    public LikeEvent(LikeDTO likeDTO) {
        this.likeDTO = likeDTO;
    }

    public LikeDTO getLikeDTO() {
        return likeDTO;
    }
}
