package G2.SafeSpace.service;

import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContextService {

    private final UserRepository userRepository;

    @Autowired
    public UserContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return Optional.ofNullable(userRepository.findByUsername(authentication.getName()));
        }
        return Optional.empty();
    }

}