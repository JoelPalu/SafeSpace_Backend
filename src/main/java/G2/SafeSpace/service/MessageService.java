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

    public SendsMessage sendMessage(int msgSender, int msgReceiver, String messageContent) {
        Optional<User> sender = userRepository.findById(msgSender);
        Optional<User> receiver = userRepository.findById(msgReceiver);
        if (sender.isPresent() && receiver.isPresent() && !messageContent.trim().isEmpty()) {
            
            //create new message
            Message newMessage = new Message();
            newMessage.setMessageContent(messageContent);
            messageRepository.save(newMessage);

            // Create the association in SendsMessage
            SendsMessage sendsMessage = new SendsMessage(sender.get(), receiver.get(), newMessage);
            return sendsMessageRepository.save(sendsMessage);
        }
        return null;
    }

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

    // Goes through the list of conversation and finds the specific one
    // where the given user is the participant
    public ConversationDTO getConversation(User withUser, List<ConversationDTO> conversations) {
        if (withUser != null) {
            for (ConversationDTO conversation : conversations) {
                int convoUserId = conversation.getWithUser().getId();
                int targetUserId = withUser.getUserID();

//                System.out.println("with user id: " + targetUserId +
//                        ", convo user id: " + convoUserId +
//                        " same? " + (convoUserId == targetUserId));

                // return the existing conversation
                if (convoUserId == targetUserId) {
                    return conversation;
                }
            }
        } else {
            System.out.println("Getting conversation failed due to user being null!");
        }
        return null;
    }

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

    public boolean updateMessage(MessageUpdate message) {
        Optional<Message> currentMessage = messageRepository.findById(message.getMessageID());
        if (currentMessage.isPresent()) {
            currentMessage.get().setMessageContent(message.getMessage());
            messageRepository.save(currentMessage.get());
            return true;
        }
        return false;
    }

    public boolean isMessageOwner(int messageID, User sender) {
        Optional<Message> message = messageRepository.findById(messageID);
        if (message.isPresent()) {
            SendsMessage result = sendsMessageRepository.findByMessage(message.get());
            return result.getSender().equals(sender);
        }
        return false;
    }

}