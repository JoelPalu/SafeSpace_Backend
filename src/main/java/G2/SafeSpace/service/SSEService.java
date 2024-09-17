package G2.SafeSpace.service;

import G2.SafeSpace.entity.Post;
import G2.SafeSpace.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

import java.util.List;

@Service
public class SSEService {
    Sinks.Many<Post> sink = Sinks.many().multicast().onBackpressureBuffer();
    private final PostRepository postRepository;


    public SSEService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostConstruct
    public void init() {
        List<Post> initialPosts = postRepository.findAll();
        emitPosts(initialPosts);
    }

    public Flux<Post> getPostFlux() {
        return sink.asFlux();
    }


    public void emitNewPost(Post post) {
        sink.tryEmitNext(post);
    }

    public void emitPosts(List<Post> posts) {
        posts.forEach(this::emitNewPost);
    }
}
