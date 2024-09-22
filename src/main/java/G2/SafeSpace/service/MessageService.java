package G2.SafeSpace.service;

import G2.SafeSpace.entity.Message;
import G2.SafeSpace.entity.SendsMessage;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.MessageRepository;
import G2.SafeSpace.repository.SendsMessageRepository;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}