package G2.SafeSpace.service;

import G2.SafeSpace.dto.MessageDTO;
import G2.SafeSpace.dto.MessageUpdate;
import G2.SafeSpace.dto.MessagesDTO;
import G2.SafeSpace.entity.Message;
import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.repository.SendsMessageRepository;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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