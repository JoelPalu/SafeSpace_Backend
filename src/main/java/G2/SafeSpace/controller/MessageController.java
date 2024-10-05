package G2.SafeSpace.controller;

import G2.SafeSpace.dto.ConversationDTO;
import G2.SafeSpace.dto.MessageUpdate;
import G2.SafeSpace.dto.MessagesDTO;
import G2.SafeSpace.entity.Message;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.service.MessageService;
import G2.SafeSpace.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.ok(messageService.getMessages(optionalUser.get()));
    }

    @DeleteMapping("/message/delete/{messageID}")
    public ResponseEntity<String> deleteMessage(@PathVariable int messageID) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Message> message = messageRepository.findById(messageID);
        if (message.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found.");
        }
        if (messageService.deleteMessage(optionalUser.get(), message.get())) {
            if (messageRepository.existsById(messageID)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message deletion failed");
            }
            return ResponseEntity.ok("Message deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not the message owner.");
        }
    }

    // returns list of conversations that the user has had.
     @GetMapping("/message/conversations")
     public ResponseEntity<List<ConversationDTO>> getConversations() {
         Optional<User> optionalUser = getCurrentUser();
         if (optionalUser.isEmpty()) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
         List<ConversationDTO> conversationDTO = messageService.getConversations(optionalUser.get());
         if (conversationDTO == null) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
         }
         return ResponseEntity.ok(messageService.getConversations(optionalUser.get()));
     }

    @PutMapping("/message/update")
    public ResponseEntity<String> updateMessage(@RequestBody MessageUpdate message) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!messageService.isMessageOwner(message.getMessageID(), optionalUser.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not the message owner.");
        }
        if (messageService.updateMessage(message)) {
            return ResponseEntity.ok("Message updated successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}