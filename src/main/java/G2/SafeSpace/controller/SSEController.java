package G2.SafeSpace.controller;

import G2.SafeSpace.entity.Post;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

@RestController
@RequestMapping("/posts")
public class SSEController {

    @GetMapping("/")
        public Flux<ServerSentEvent<String>> getEvents () throws InterruptedException, IOException {
        ArrayList<Post> posts = new ArrayList<>();

        return Flux.interval(Duration.ofSeconds(10))
                .map(seq -> ServerSentEvent.<String>builder()
                        .data("Event #" + seq)
                        .build());
    }
}

