package G2.SafeSpace.controller;

import G2.SafeSpace.dto.MessageDTO;
import G2.SafeSpace.dto.MessagesDTO;
import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.repository.SendsMessageRepository;
import G2.SafeSpace.service.MessageService;
import G2.SafeSpace.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserContextService userContextService;
    private final MessageService messageService;
    private final SendsMessageRepository sendsMessageRepository;

    public MessageController(MessageRepository messageRepository,
                             UserContextService userContextService,
                             MessageService messageService, SendsMessageRepository sendsMessageRepository) {
        this.messageRepository = messageRepository;
        this.userContextService = userContextService;
        this.messageService = messageService;
        this.sendsMessageRepository = sendsMessageRepository;
    }

    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

    @PostMapping("/message/send")
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

    @GetMapping("/message")
    public ResponseEntity<MessagesDTO> getMessages() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Fetch sent and received messages
        List<SendsMessage> sentMessages = sendsMessageRepository.findBySender(optionalUser.get());
        List<SendsMessage> receivedMessages = sendsMessageRepository.findByReceiver(optionalUser.get());

        // Convert to DTOs
        List<MessageDTO> sentMessageDTOs = sentMessages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        List<MessageDTO> receivedMessageDTOs = receivedMessages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        // Return both lists in a single DTO
        MessagesDTO messagesDTO = new MessagesDTO(sentMessageDTOs, receivedMessageDTOs);

        return ResponseEntity.ok(messagesDTO);
    }

    @DeleteMapping("/message/delete")
    public ResponseEntity<String> deleteMessage(@RequestParam int messageID) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        messageService.deleteMessage(optionalUser.get(), messageID);
        return ResponseEntity.ok("Message deleted successfully!");
    }

}