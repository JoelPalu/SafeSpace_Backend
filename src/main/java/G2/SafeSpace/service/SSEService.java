package G2.SafeSpace.service;

import G2.SafeSpace.dto.FriendshipDTO;
import G2.SafeSpace.dto.LikeDTO;
import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.event.FriendrequestEvent;
import G2.SafeSpace.event.LikeEvent;
import G2.SafeSpace.event.PostCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for handling Server-Sent Events (SSE) for real-time notifications
 * related to posts, likes, and friend requests. This service emits events to connected
 * clients whenever a post is created, a like is added, or a friendship event occurs.
 * <p>
 * The service manages sinks for broadcasting posts, likes, and friendship requests
 * to multiple subscribers. It allows clients to subscribe to these streams and receive
 * real-time notifications.
 * </p>
 */
@Service
public class SSEService {
    // global feed
    private final Sinks.Many<PostDTO> postSink = Sinks.many().multicast().onBackpressureBuffer();
    // global likes, could be transformed to unicast
    // if each user should only receive specified like notifications
    private final Sinks.Many<LikeDTO> likeSink = Sinks.many().multicast().onBackpressureBuffer();
    // personal data
    private final Map<String, Sinks.Many<FriendshipDTO>> friendRequestSinks = new ConcurrentHashMap<>();
    //private final Map<String, Sinks.Many<LikeDTO>> likeNotification = new ConcurrentHashMap<>();
    //private final Map<String, Sinks.Many<MessageDTO>> messageSinks = new ConcurrentHashMap<>();
    //private final Map<String, Sinks.Many<CommentDTO>> commentSinks = new ConcurrentHashMap<>();

    // PERSONAL SINKS

    /**
     * Retrieves or creates a sink for friendship requests for a specific user.
     * <p>
     * This method ensures that each user has a unique sink to handle their friendship requests.
     * </p>
     *
     * @param userId The ID of the user for whom the friendship request sink is required.
     * @return A sink for the friendship requests for the given user.
     */
    private Sinks.Many<FriendshipDTO> getFriendRequestSink(String userId) {
        return friendRequestSinks.computeIfAbsent(userId, id -> Sinks.many().unicast().onBackpressureBuffer());
    }

    //private Sinks.Many<MessageDTO> getMessageSink(String userId) {
     //   return messageSinks.computeIfAbsent(userId, id -> Sinks.many().unicast().onBackpressureBuffer());
    //}

    //private Sinks.Many<CommentDTO> getCommentSink(String userId) {
    //    return commentSinks.computeIfAbsent(userId, id -> Sinks.many().unicast().onBackpressureBuffer());
    //}

    /**
     * Event listener for handling post creation events. When a post is created, this method emits
     * the new post event to the global post stream.
     *
     * @param event The event containing information about the newly created post.
     */
    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent event) {
        emitNewPost(event.getPost());
    }

    /**
     * Event listener for handling like events. When a like is added, this method emits the
     * like event to the global like stream.
     *
     * @param event The event containing information about the added like.
     */
    @EventListener
    public void handleLikeAddedEvent(LikeEvent event) {
        likeSink.tryEmitNext(event.getLikeDTO());
    }

    /**
     * Event listener for handling friendship events. When a friendship request occurs, this method
     * emits the event to the sinks of both the requester and the receiver.
     *
     * @param event The event containing information about the friendship request.
     */
    @EventListener
    public void handleFriendshipEvent(FriendrequestEvent event) {
        FriendshipDTO friendshipDTO = event.getFriendshipDTO();
        String requesterId = String.valueOf(friendshipDTO.getRequestingUserId());
        String receiverId = String.valueOf(friendshipDTO.getReceivingUserId());

        // Get or create sinks for both users
        Sinks.Many<FriendshipDTO> friendRequestSink = getFriendRequestSink(requesterId);
        Sinks.Many<FriendshipDTO> friendReceiverSink = getFriendRequestSink(receiverId);

        // Emit the event to both users
        if (friendRequestSink != null && friendReceiverSink != null) {
            friendRequestSink.tryEmitNext(friendshipDTO);
            friendReceiverSink.tryEmitNext(friendshipDTO);
        } else {
            System.out.println("Friendship sink failed");
        }
    }

    /**
     * Combines the flux streams for post events and like events into a single flux
     * stream that can be subscribed to by clients for receiving real-time notifications.
     *
     * @return A combined flux stream of Server-Sent Events for posts and likes.
     */
    public Flux<ServerSentEvent<?>> getCombinedFlux() {
        Flux<ServerSentEvent<PostDTO>> postEvents = postSink.asFlux()
                .map(post -> ServerSentEvent.<PostDTO>builder()
                        .data(post)
                        .event(post.getEventType())
                        .build());

        Flux<ServerSentEvent<LikeDTO>> likeEvents = likeSink.asFlux()
                .map(like -> ServerSentEvent.<LikeDTO>builder()
                        .data(like)
                        .event(like.getEventType())
                        .build());

        return Flux.merge(postEvents, likeEvents);
    }

    /**
     * Emits a new post to the global post stream.
     * <p>
     * This method adds a new post to the post stream, making it available for clients
     * subscribed to the post feed.
     * </p>
     *
     * @param postDTO The PostDTO object representing the new post to emit.
     */
    public void emitNewPost(PostDTO postDTO) {
        postSink.tryEmitNext(postDTO);
    }
}
