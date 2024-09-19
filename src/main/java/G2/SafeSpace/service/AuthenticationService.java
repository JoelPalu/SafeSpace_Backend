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

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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

    public UserDTO register(AuthenticationRequest request) {

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        user.setBio(null);
        user.setProfilePictureID("default");

        String token = jwtService.generateToken(user, generateExtraClaims(user));
        User savedUser = userRepository.save(user);
        UserDTO userDTO = new UserDTO(savedUser, false);
        userDTO.setJwt(token);

        return userDTO;
    }


    public UserDTO login(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        authenticationManager.authenticate(token);
        User user = userRepository.findByUsername(request.getUsername());
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));

        UserDTO userDTO = new UserDTO(user, false);
        userDTO.setJwt(jwt);

        return userDTO;


    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("id", user.getUserID());
        return extraClaims;

    }
}
