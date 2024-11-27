package G2.SafeSpace.service;

import G2.SafeSpace.dto.*;
import G2.SafeSpace.entity.Message;
import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.repository.SendsMessageRepository;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@code MessageService} class provides services for managing messages between users.
 * It includes functionalities for sending messages, retrieving conversations, updating and deleting messages,
 * and checking ownership of messages.
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SendsMessageRepository sendsMessageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          SendsMessageRepository sendsMessageRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.sendsMessageRepository = sendsMessageRepository;
    }

    /**
     * Sends a message from one user to another and creates an association in the {@link SendsMessage} entity.
     *
     * @param msgSender the ID of the sender user
     * @param msgReceiver the ID of the receiver user
     * @param messageContent the content of the message
     * @return the {@link SendsMessage} entity representing the message association, or {@code null} if an error occurred
     */
    public SendsMessage sendMessage(int msgSender, int msgReceiver, String messageContent) {
        Optional<User> sender = userRepository.findById(msgSender);
        Optional<User> receiver = userRepository.findById(msgReceiver);
        if (sender.isPresent() && receiver.isPresent() && !messageContent.trim().isEmpty()) {

            // Create new message
            Message newMessage = new Message();
            newMessage.setMessageContent(messageContent);
            messageRepository.save(newMessage);

            // Create the association in SendsMessage
            SendsMessage sendsMessage = new SendsMessage(sender.get(), receiver.get(), newMessage);
            return sendsMessageRepository.save(sendsMessage);
        }
        return null;
    }

    /**
     * Retrieves a list of all conversations for the specified user, including the messages exchanged.
     *
     * @param user the user whose conversations are to be retrieved
     * @return a list of {@link ConversationDTO} objects representing the user's conversations
     */
    public List<ConversationDTO> getConversations(User user) {
        // Get all the messages sent or received by the user
        List<SendsMessage> allMessages = sendsMessageRepository.findBySenderOrReceiver(user, user);

        // Initialize new list that will be filled with all the conversations
        List<ConversationDTO> conversations = new ArrayList<>();

        // Loop through all the messages and create conversation for each new user.
        for (SendsMessage message : allMessages) {

            // mark the participant of the conversation
            User participant = null;
            // flag if user is sender or receiver
            boolean isSender = false;

            if (message.getSender().equals(user)) {
                isSender = true;
                participant = message.getReceiver();
            } else if (message.getReceiver().equals(user)) {
                participant = message.getSender();
            }

            // break the loop if participant is null (this should never happen)
            if(participant == null) {
                return null;
            }

            // initialize the first dto before comparing
            if (conversations.isEmpty()) {
                ConversationDTO conversationDTO = new ConversationDTO();
                conversationDTO.setWithUser(new UserDTO(participant, false));
                conversationDTO.addMessage(new ConversationMessage(message, isSender));
                conversationDTO.incrementMessageCount();
                conversations.add(conversationDTO);

                // after the 1st initialization, rest of the messages get compared
                // create new conversation if there is not one with participant / add message to existing conversation
            } else {
                ConversationDTO conversationDTO = getConversation(participant, conversations);
                if (conversationDTO == null) {
                    conversationDTO = new ConversationDTO();
                    conversationDTO.setWithUser(new UserDTO(participant, false));
                    conversationDTO.incrementMessageCount();
                    conversationDTO.addMessage(new ConversationMessage(message, isSender));
                    conversations.add(conversationDTO);
                } else {
                    conversationDTO.addMessage(new ConversationMessage(message, isSender));
                    conversationDTO.incrementMessageCount();
                }
            }
        }
        return conversations;
    }

    /**
     * Finds a specific conversation in the list of conversations where the given user is a participant.
     *
     * @param withUser the user to find the conversation with
     * @param conversations the list of all conversations
     * @return the {@link ConversationDTO} for the specified user, or {@code null} if no conversation is found
     */
    public ConversationDTO getConversation(User withUser, List<ConversationDTO> conversations) {
        if (withUser != null) {
            for (ConversationDTO conversation : conversations) {
                int convoUserId = conversation.getWithUser().getId();
                int targetUserId = withUser.getUserID();

                // return the existing conversation if found
                if (convoUserId == targetUserId) {
                    return conversation;
                }
            }
        } else {
            System.out.println("Getting conversation failed due to user being null!");
        }
        return null;
    }

    /**
     * Retrieves all sent and received messages for a user in the form of {@link MessageDTO} objects.
     *
     * @param user the user whose messages are to be retrieved
     * @return a {@link MessagesDTO} object containing the lists of sent and received messages
     */
    public MessagesDTO getMessages(User user) {
        // Fetch sent and received messages
        List<SendsMessage> sentMessages = sendsMessageRepository.findBySender(user);
        List<SendsMessage> receivedMessages = sendsMessageRepository.findByReceiver(user);

        // Convert to DTOs
        List<MessageDTO> sentMessageDTOs = sentMessages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        List<MessageDTO> receivedMessageDTOs = receivedMessages.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        // Return both lists in a single DTO
        return new MessagesDTO(sentMessageDTOs, receivedMessageDTOs);
    }

    /**
     * Deletes a message if the provided sender is the owner of the message.
     *
     * @param msgSender the user attempting to delete the message
     * @param message the {@link Message} to be deleted
     * @return {@code true} if the message was deleted successfully, {@code false} otherwise
     */
    public boolean deleteMessage(User msgSender, Message message) {
        SendsMessage result = sendsMessageRepository.findByMessage(message);
        if (result != null) {
            if (result.getSender().equals(msgSender)) {
                sendsMessageRepository.delete(result);
                messageRepository.delete(message);
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Updates the content of an existing message.
     *
     * @param message the {@link MessageUpdate} DTO containing the message ID and updated content
     * @return {@code true} if the message was updated successfully, {@code false} otherwise
     */
    public boolean updateMessage(MessageUpdate message) {
        Optional<Message> currentMessage = messageRepository.findById(message.getMessageID());
        if (currentMessage.isPresent()) {
            currentMessage.get().setMessageContent(message.getMessage());
            messageRepository.save(currentMessage.get());
            return true;
        }
        return false;
    }

    /**
     * Checks if the given user is the owner of the message with the specified ID.
     *
     * @param messageID the ID of the message
     * @param sender the user attempting to check ownership of the message
     * @return {@code true} if the user is the owner of the message, {@code false} otherwise
     */
    public boolean isMessageOwner(int messageID, User sender) {
        Optional<Message> message = messageRepository.findById(messageID);
        if (message.isPresent()) {
            SendsMessage result = sendsMessageRepository.findByMessage(message.get());
            return result.getSender().equals(sender);
        }
        return false;
    }

}