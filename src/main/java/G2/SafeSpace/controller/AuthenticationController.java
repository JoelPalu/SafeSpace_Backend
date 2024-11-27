/**
 * REST controller for handling authentication-related operations, including user registration and login.
 */
package G2.SafeSpace.controller;

import G2.SafeSpace.dto.AuthenticationRequest;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.service.AuthenticationService;
import G2.SafeSpace.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests for authentication endpoints such as user registration and login.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Constructor to initialize the AuthenticationController with the required services.
     *
     * @param authenticationService the service handling authentication logic.
     * @param userService the service handling user-related logic.
     */
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * Endpoint for user registration.
     *
     * @param request the {@link AuthenticationRequest} containing the username and password.
     * @return a {@link ResponseEntity} containing a {@link UserDTO} with user details if successful,
     *         or an appropriate HTTP status code for failure scenarios.
     *         <ul>
     *             <li>{@code 201 Created} - if registration is successful.</li>
     *             <li>{@code 409 Conflict} - if the username is already in use.</li>
     *             <li>{@code 400 Bad Request} - if the username or password is missing or invalid.</li>
     *         </ul>
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody AuthenticationRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            if (userService.isUsernameAvailable(request.getUsername())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * Endpoint for user login.
     *
     * @param request the {@link AuthenticationRequest} containing the username and password.
     * @return a {@link ResponseEntity} containing a {@link UserDTO} with user details and a JWT token if successful,
     *         or an appropriate HTTP status code for failure scenarios.
     *         <ul>
     *             <li>{@code 200 OK} - if login is successful.</li>
     *             <li>{@code 400 Bad Request} - if the username or password is invalid.</li>
     *         </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody AuthenticationRequest request) {
        if (userService.findUserByUsername(request.getUsername()) != null && request.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(request));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
