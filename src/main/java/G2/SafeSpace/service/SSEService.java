package G2.SafeSpace.service;

import G2.SafeSpace.dto.PostDTO;
import G2.SafeSpace.event.PostCreatedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

import java.util.List;

@Service
public class SSEService implements ApplicationListener<PostCreatedEvent> {
    Sinks.Many<PostDTO> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final PostService postService;


    public SSEService(PostService postService) {
        this.postService = postService;
    }

    @PostConstruct
    public void init() {
        List<PostDTO> initialPosts = postService.findAllPosts();
        emitPosts(initialPosts);
    }

    @Override
    public void onApplicationEvent(PostCreatedEvent event) {
        emitNewPost(event.getPost());
    }

    public Flux<PostDTO> getPostFlux() {
        return sink.asFlux();
    }

    public void emitNewPost(PostDTO postDTO) {
        sink.tryEmitNext(postDTO);
    }

    public void emitPosts(List<PostDTO> posts) {
        posts.forEach(this::emitNewPost);
    }
}
