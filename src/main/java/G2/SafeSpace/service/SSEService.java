package G2.SafeSpace.service;

import G2.SafeSpace.dto.FriendshipDTO;
import G2.SafeSpace.dto.LikeDTO;
import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.event.FriendrequestEvent;
import G2.SafeSpace.event.LikeEvent;
import G2.SafeSpace.event.PostCreatedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    private final PostService postService;


    public SSEService(PostService postService) {
        this.postService = postService;
    }

    @PostConstruct
    public void init() {
        List<PostDTO> initialPosts = postService.findAllPosts();
        emitPosts(initialPosts);
    }

    // PERSONAL SINKS

    private Sinks.Many<FriendshipDTO> getFriendRequestSink(String userId) {
        return friendRequestSinks.computeIfAbsent(userId, id -> Sinks.many().unicast().onBackpressureBuffer());
    }

    //private Sinks.Many<MessageDTO> getMessageSink(String userId) {
     //   return messageSinks.computeIfAbsent(userId, id -> Sinks.many().unicast().onBackpressureBuffer());
    //}

    //private Sinks.Many<CommentDTO> getCommentSink(String userId) {
    //    return commentSinks.computeIfAbsent(userId, id -> Sinks.many().unicast().onBackpressureBuffer());
    //}

    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent event) {
        emitNewPost(event.getPost());
    }

    @EventListener
    public void handleLikeAddedEvent(LikeEvent event) {
        likeSink.tryEmitNext(event.getLikeDTO());
    }

    @EventListener
    public void handleFriendshipEvent(FriendrequestEvent event) {
        FriendshipDTO friendshipDTO = event.getFriendshipDTO();
        String requesterId = String.valueOf(friendshipDTO.getRequestingUserId());
        String recieverId = String.valueOf(friendshipDTO.getRecievingUserId());

        // Get or create sinks for both users
        Sinks.Many<FriendshipDTO> friendRequestSink = getFriendRequestSink(requesterId);
        Sinks.Many<FriendshipDTO> friendRecieverSink = getFriendRequestSink(recieverId);

        // Emit the event to both users
        if (friendRequestSink != null && friendRecieverSink != null) {
            friendRequestSink.tryEmitNext(friendshipDTO);
            friendRecieverSink.tryEmitNext(friendshipDTO);
        } else {
            System.out.println("Friendship sink failed");
        }
    }

    public Flux<ServerSentEvent<?>> getCombinedFlux() {
        Flux<ServerSentEvent<?>> postEvents = postSink.asFlux()
                .map(post -> ServerSentEvent.<PostDTO>builder()
                        .data(post)
                        .event(post.getEventType())
                        .build());

        Flux<ServerSentEvent<?>> likeEvents = likeSink.asFlux()
                .map(like -> ServerSentEvent.<LikeDTO>builder()
                        .data(like)
                        .event(like.getEventType())
                        .build());

        return Flux.merge(postEvents, likeEvents);
    }


    public void emitNewPost(PostDTO postDTO) {
        postSink.tryEmitNext(postDTO);
    }

    //populates sse with all the current posts in db during initialization
    //this could also just be transformed to client side (getallposts()) once the user logs in?
    public void emitPosts(List<PostDTO> posts) {
        posts.forEach(post -> {
            post.setEventType("initial_post");
            emitNewPost(post);
        });
    }
}
