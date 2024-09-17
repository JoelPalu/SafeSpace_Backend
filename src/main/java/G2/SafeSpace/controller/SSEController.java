package G2.SafeSpace.controller;

import G2.SafeSpace.entity.Post;
import G2.SafeSpace.service.SSEService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/posts")
public class SSEController {
    private final SSEService sseService;

    public SSEController(SSEService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/events", produces = "text/event-stream")
    public Flux<ServerSentEvent<String>> getAllPosts() {
        Flux<Post> posts = sseService.getPostFlux();

        return posts.map(post -> ServerSentEvent.builder(post.toString()).build());
    }
}

