package G2.SafeSpace.controller;

import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.service.MessageService;
import G2.SafeSpace.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserContextService userContextService;
    private final MessageService messageService;

    public MessageController(MessageRepository messageRepository,
                             UserContextService userContextService,
                             MessageService messageService) {
        this.messageRepository = messageRepository;
        this.userContextService = userContextService;
        this.messageService = messageService;
    }

    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam int toUserId, @RequestParam String messageContent) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (messageService.sendMessage(optionalUser.get().getUserID(), toUserId, messageContent) != null) {
            return ResponseEntity.ok("Message sent successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}