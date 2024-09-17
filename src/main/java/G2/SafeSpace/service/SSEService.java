package G2.SafeSpace.service;

import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.event.PostCreatedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

import java.util.List;

@Service
public class SSEService implements ApplicationListener<PostCreatedEvent> {
    // global
    private final Sinks.Many<PostDTO> postSink = Sinks.many().multicast().onBackpressureBuffer();
    // personal data
    //private final Map<String, Sinks.Many<MessageDTO>> messageSinks = new ConcurrentHashMap<>();
    //private final Map<String, Sinks.Many<FriendRequestDTO>> friendRequestSinks = new ConcurrentHashMap<>();
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

    //private Sinks.Many<MessageDTO> getMessageSink(String userId) {
     //   return messageSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().onBackpressureBuffer());
    //}

    //private Sinks.Many<FriendRequestDTO> getFriendRequestSink(String userId) {
     //   return friendRequestSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().onBackpressureBuffer());
    //}

    //private Sinks.Many<CommentDTO> getCommentSink(String userId) {
    //    return commentSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().onBackpressureBuffer());
    //}

    @Override
    public void onApplicationEvent(PostCreatedEvent event) {
        emitNewPost(event.getPost());
    }

    //this will be combined later (e.g. combined flux) to send all data to same stream
    public Flux<ServerSentEvent<PostDTO>> getPostFlux() {
        return postSink.asFlux()
                .map(post -> ServerSentEvent.<PostDTO>builder()
                        .data(post)
                        .event("post")
                        .build());
    }


    public void emitNewPost(PostDTO postDTO) {
        postSink.tryEmitNext(postDTO);
    }

    //populates sse with all the current posts in db during initialization
    //this could also just be transformed to client side (getallposts()) once the user logs in?
    public void emitPosts(List<PostDTO> posts) {
        posts.forEach(this::emitNewPost);
    }
}
