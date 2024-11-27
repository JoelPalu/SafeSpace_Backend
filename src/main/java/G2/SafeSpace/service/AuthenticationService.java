package G2.SafeSpace.service;

import G2.SafeSpace.dto.AuthenticationRequest;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.config.JwtService;
import G2.SafeSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code AuthenticationService} class provides authentication functionality for user registration and login.
 * It interacts with the {@link UserRepository} to handle user data, the {@link JwtService} to manage JWT tokens,
 * and the {@link AuthenticationManager} for authentication processing. This service is responsible for authenticating
 * users, registering new users, and generating JWT tokens for authenticated users.
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs an {@code AuthenticationService} instance with the required dependencies.
     *
     * @param authenticationManager the {@link AuthenticationManager} to handle authentication
     * @param userRepository the {@link UserRepository} for interacting with user data
     * @param jwtService the {@link JwtService} for JWT token generation
     * @param passwordEncoder the {@link PasswordEncoder} to encode user passwords
     */
    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 UserRepository userRepository,
                                 JwtService jwtService,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user by accepting an {@link AuthenticationRequest} and creating a new {@link User} entity.
     * The user's password is encoded, and a JWT token is generated for the newly created user.
     *
     * @param request the {@link AuthenticationRequest} containing the user's registration information
     * @return a {@link UserDTO} containing the new user's details and JWT token
     */
    public UserDTO register(AuthenticationRequest request) {

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        user.setBio(null);
        user.setProfilePictureID("default");

        // Generate JWT token for the user
        String token = jwtService.generateToken(user, generateExtraClaims(user));
        // Save the user to the repository
        User savedUser = userRepository.save(user);
        // Create a DTO for the saved user
        UserDTO userDTO = new UserDTO(savedUser, false);
        userDTO.setJwt(token);

        return userDTO;
    }

    /**
     * Authenticates a user by accepting an {@link AuthenticationRequest} containing their username and password.
     * The user is authenticated using {@link AuthenticationManager}, and a JWT token is generated for the user.
     *
     * @param request the {@link AuthenticationRequest} containing the user's login credentials
     * @return a {@link UserDTO} containing the authenticated user's details and JWT token
     */
    public UserDTO login(AuthenticationRequest request) {
        // Create authentication token for the user
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        // Authenticate the user
        authenticationManager.authenticate(token);
        // Retrieve the user from the database
        User user = userRepository.findByUsername(request.getUsername());
        // Generate JWT token for the authenticated user
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        // Create a DTO for the authenticated user
        UserDTO userDTO = new UserDTO(user, false);
        userDTO.setJwt(jwt);

        return userDTO;
    }

    /**
     * Generates additional claims to be included in the JWT token.
     * This method adds the user's username and ID as extra claims.
     *
     * @param user the {@link User} whose information will be included in the JWT claims
     * @return a map containing extra claims (username and user ID)
     */
    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("id", user.getUserID());
        return extraClaims;
    }
}
