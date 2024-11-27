package G2.SafeSpace.service;

import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing and retrieving the current authenticated user context.
 * <p>
 * This service interacts with the security context to retrieve the currently authenticated user's details
 * from the `SecurityContextHolder`, and provides methods to access the user entity from the repository
 * based on the authenticated username.
 * </p>
 */
@Service
public class UserContextService {

    private final UserRepository userRepository;

    /**
     * Constructor to initialize the UserContextService with the UserRepository.
     *
     * @param userRepository The repository for accessing user data.
     */
    @Autowired
    public UserContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the current authenticated user.
     * <p>
     * This method checks the current authentication object and uses the username from the authentication
     * to fetch the corresponding user entity from the `UserRepository`. If no authenticated user is found,
     * it returns an empty {@link Optional}.
     * </p>
     *
     * @return An {@link Optional} containing the {@link User} if the user is authenticated, or an empty
     *         {@link Optional} if no authenticated user is found.
     */
    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return Optional.ofNullable(userRepository.findByUsername(authentication.getName()));
        }
        return Optional.empty();
    }
}