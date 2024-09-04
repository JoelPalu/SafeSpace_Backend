package G2.SafeSpace.service;

import G2.SafeSpace.authentication.AuthenticationRequest;
import G2.SafeSpace.authentication.AuthenticationResponse;
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

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 UserService userService,
                                 JwtService jwtService,
                                 PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthenticationResponse register(User request) {

        request.setUsername(request.getUsername().trim());
        request.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        request.setBio(null);
        request.setProfilePictureID("default");

        String token = jwtService.generateToken(request, generateExtraClaims(request));
        return new AuthenticationResponse(token , userService.save(request));
    }


    public AuthenticationResponse login(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        authenticationManager.authenticate(token);
        User user = userService.findUserByUsername(request.getUsername());
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(jwt, user);


    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("id", user.getUserID());
        return extraClaims;

    }
}
