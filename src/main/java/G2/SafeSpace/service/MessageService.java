package G2.SafeSpace.service;

import G2.SafeSpace.dto.MessageSendRequest;
import G2.SafeSpace.entity.Message;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public boolean sendMessage(MessageSendRequest request) {
        Optional<User> sender = userRepository.findById(request.getSenderID());
        Optional<User> receiver = userRepository.findById(request.getReceiverID());
        String message = request.getMessage();
        if (sender.isPresent() && receiver.isPresent() && !request.getMessage().trim().isEmpty()) {
            Message newMessage = new Message();
            newMessage.setMessageContent(message);
            messageRepository.saveAndFlush(newMessage);
            //update jointable here with id's
            return true;
        }
        return false;
    }

    public boolean deleteMessage(int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateMessage(int id, Message updatedMessage) {
        if (messageRepository.existsById(id)) {
            messageRepository.save(updatedMessage);
            return true;
        } else {
            return false;
        }
    }
}