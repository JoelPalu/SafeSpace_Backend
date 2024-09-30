package G2.SafeSpace.controller;

import G2.SafeSpace.dto.AuthenticationRequest;
import G2.SafeSpace.dto.UserDTO;
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

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody AuthenticationRequest request) {
        if (userService.findUserByUsername(request.getUsername()) != null && request.getPassword() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.login(request));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
