package G2.SafeSpace.event;

import G2.SafeSpace.dto.PostDTO;
import org.springframework.context.ApplicationEvent;

/**
 * The {@code PostCreatedEvent} class represents an event triggered when a new post is created.
 * This event contains the {@code PostDTO} object that encapsulates the details of the newly created post.
 * It extends {@code ApplicationEvent} to be published within the Spring application context.
 */
public class PostCreatedEvent extends ApplicationEvent {

    /**
     * The {@code PostDTO} object that holds the details of the newly created post.
     */
    private final PostDTO post;

    /**
     * Constructs a new {@code PostCreatedEvent} with the specified source and {@code PostDTO}.
     * The event type is set to "new_post" upon creation.
     *
     * @param source the source of the event (typically the object that triggered the event)
     * @param post the {@code PostDTO} containing the details of the new post
     */
    public PostCreatedEvent(Object source, PostDTO post) {
        super(source);
        this.post = post;
        post.setEventType("new_post");
    }

    /**
     * Returns the {@code PostDTO} associated with this event, containing the details of the newly created post.
     *
     * @return the {@code PostDTO} object representing the new post
     */
    public PostDTO getPost() {
        return post;
    }
}