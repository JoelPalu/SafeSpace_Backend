package G2.SafeSpace.service;

import G2.SafeSpace.authentication.AuthenticationRequest;
import G2.SafeSpace.authentication.AuthenticationResponse;
import G2.SafeSpace.config.JwtService;
import G2.SafeSpace.dto.UpdateUserResponse;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import G2.SafeSpace.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserContextService userContextService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.userContextService = userContextService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<User> findAllUsers() {
        try {
            return userRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not find all users " + e.getMessage());
        }
    }

    public boolean checkUsernameAvailability(String username) {
        List<User> users = findAllUsers();
        boolean taken = false;
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    taken = true;
                    break;
                }
            }
            return !taken;
        }
        return false;
    }



    public User findUserById(int id) {
        try {
            Optional<User> user = userRepository.findById(id);
            return user.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id" + id + " " + e.getMessage());
        }
    }

    public UpdateUserResponse updateUser(User updatedUser) {
        try {
            Optional<User> existingUserOptional = userContextService.getCurrentUser();
            if (existingUserOptional.isPresent()) {

                String username = updatedUser.getUsername();
                String password = updatedUser.getPassword();
                String bio = updatedUser.getBio();
                String profilepictureID = updatedUser.getProfilePictureID();

                User existingUser = existingUserOptional.get();

                if (!existingUser.getUsername().equals(username) && username != null && !username.trim().isEmpty()) {
                    if (checkUsernameAvailability(username)) {
                        existingUser.setUsername(username.trim());
                    } else {
                        return new UpdateUserResponse(true, false, null);
                    }
                }
                if (!existingUser.getPassword().equals(password) && password != null && !password.trim().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(password.trim()));
                }

                //bio updating, null should be sent if no changes were made
                //empty string if user wants to clear the bio
                if (bio != null && !bio.equals(existingUser.getBio())) {
                    if (bio.trim().isEmpty()) {
                        existingUser.setBio(null);
                    } else {
                        existingUser.setBio(bio.trim());
                    }
                }

                if (!existingUser.getProfilePictureID().equals(profilepictureID) && profilepictureID != null) {
                    existingUser.setProfilePictureID(profilepictureID);
                }

                User savedUser = userRepository.save(existingUser);

                UserDTO userDTO = new UserDTO();
                userDTO.setId(savedUser.getUserID());
                userDTO.setUsername(savedUser.getUsername());
                userDTO.setBio(savedUser.getBio());
                userDTO.setProfilePictureID(savedUser.getProfilePictureID());
                userDTO.setJwt(jwtService.generateToken(savedUser, generateExtraClaims(savedUser)));

                return new UpdateUserResponse(false, true, userDTO);
            } else return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user " + e.getMessage());
        }
    }

    public boolean deleteUser() {
        try {
            Optional<User> existingUserOptional = userContextService.getCurrentUser();
            if (existingUserOptional.isPresent()) {
                userRepository.deleteById(existingUserOptional.get().getUserID());
                return true;
            } else return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user " + e.getMessage());
        }
    }

    public User findUserByUsername(String username) {
        try {
            List<User> optionalUsers = userRepository.findAll();
            if (!optionalUsers.isEmpty()) {
                for (User user : optionalUsers) {
                    if (user.getUsername().equalsIgnoreCase(username)) {
                        return user;
                    }
                }
            } return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user " + username + " " + e.getMessage());
        }
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    public Optional<User> addFriend(User user, User friend) {
        try {
            user.addFriends(friend);
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add friend " + e.getMessage());
        }
    }

    public Optional<User> removeFriend(User user, User friend) {
        try {
            user.removeFriends(friend);
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove friend " + e.getMessage());
        }
    }

    public List<Integer> getFriends(User user) {
        try {
            return user.getFriends().stream()
                    .map(User::getUserID)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get friend IDs " + e.getMessage());
        }
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("id", user.getUserID());
        return extraClaims;
    }
}
