package G2.SafeSpace.service;

import G2.SafeSpace.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserContextService {

    private final UserService userService;

    @Autowired
    public UserContextService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            User currentUser = userService.findUserByUsername(authentication.getName());
            return Optional.ofNullable(currentUser);
        }
        return Optional.empty();
    }

}