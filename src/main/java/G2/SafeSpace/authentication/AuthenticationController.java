package G2.SafeSpace.authentication;

import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import G2.SafeSpace.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
    public ResponseEntity<AuthenticationResponce> register(@RequestBody User user) {

        if (userService.findUserByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponce> authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("Menee CONTROLLERIIN!");
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
