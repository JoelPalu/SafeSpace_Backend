package G2.SafeSpace.controller;

import G2.SafeSpace.authentication.AuthenticationRequest;
import G2.SafeSpace.authentication.AuthenticationResponse;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.service.AuthenticationService;
import G2.SafeSpace.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
            if (userService.checkUsernameAvailability(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(user));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        if (userService.findUserByUsername(request.getUsername()) != null && request.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(request));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
