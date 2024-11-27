package G2.SafeSpace.controller;

import G2.SafeSpace.service.SSEService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Controller class for handling Server-Sent Events (SSE) in the application.
 * This controller exposes an endpoint to stream real-time data to the client
 * using Server-Sent Events (SSE) technology.
 */
@RestController
@RequestMapping("api/v1")
public class SSEController {

    private final SSEService sseService;

    /**
     * Constructs a new instance of SSEController.
     *
     * @param sseService the SSEService to be injected
     */
    public SSEController(SSEService sseService) {
        this.sseService = sseService;
    }

    /**
     * Endpoint to stream data as Server-Sent Events (SSE) to the client.
     * The client will receive a continuous stream of events from the server
     * in the form of ServerSentEvent objects.
     *
     * @return a Flux of ServerSentEvent objects containing the streamed data
     */
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<?>> dataStream() {
        return sseService.getCombinedFlux();
    }
}
