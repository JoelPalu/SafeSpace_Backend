package G2.SafeSpace.event;

import G2.SafeSpace.dto.PostDTO;
import org.springframework.context.ApplicationEvent;

public class PostCreatedEvent extends ApplicationEvent {
    private final PostDTO post;

    public PostCreatedEvent(Object source, PostDTO post) {
        super(source);
        this.post = post;
    }

    public PostDTO getPost() {
        return post;
    }
}