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

/**
 * The {@code MessageController} class handles all message-related operations in the application.
 * This includes sending, retrieving, deleting, updating messages, and fetching conversations.
 * It relies on the {@link MessageService} for business logic and {@link MessageRepository}
 * for database interactions. It also uses {@link UserContextService} to identify the current user.
 *
 * <p>Endpoints in this controller follow the base URL pattern: {@code /api/v1}.
 *
 * <p>Note: All endpoints require the user to be authenticated.
 */
@RestController
@RequestMapping("api/v1")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserContextService userContextService;
    private final MessageService messageService;


    /**
     * Constructs a {@code MessageController} with required dependencies.
     *
     * @param messageRepository the repository to interact with {@link Message} entities
     * @param userContextService the service to get the current authenticated {@link User}
     * @param messageService the service handling message-related business logic
     */
    public MessageController(MessageRepository messageRepository,
                             UserContextService userContextService,
                             MessageService messageService) {
        this.messageRepository = messageRepository;
        this.userContextService = userContextService;
        this.messageService = messageService;
    }

    /**
     * Helper method to get the currently authenticated user.
     *
     * @return an {@link Optional} containing the {@link User} if authenticated, otherwise empty
     */
    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

    /**
     * Sends a message from the current user to the specified recipient.
     *
     * @param toUserId the ID of the recipient user
     * @param messageContent the content of the message to send
     * @return a {@code ResponseEntity} containing a success or error message
     */
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

    /**
     * Retrieves all messages for the current user.
     *
     * @return a {@code ResponseEntity} containing the user's messages wrapped in {@link MessagesDTO}
     */
    @GetMapping("/message")
    public ResponseEntity<MessagesDTO> getMessages() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(messageService.getMessages(optionalUser.get()));
    }

    /**
     * Deletes a message by its ID if the current user is the owner.
     *
     * @param messageID the ID of the message to delete
     * @return a {@code ResponseEntity} containing a success or error message
     */
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

    /**
     * Retrieves a list of conversations that the current user has participated in.
     *
     * @return a {@code ResponseEntity} containing a list of {@link ConversationDTO}
     */
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

    /**
     * Updates the content of an existing message if the current user is the owner.
     *
     * @param message the message update details wrapped in {@link MessageUpdate}
     * @return a {@code ResponseEntity} containing a success or error message
     */
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