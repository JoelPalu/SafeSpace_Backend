package G2.SafeSpace.controller;

import G2.SafeSpace.dto.UpdateUserDTO;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.dto.UserDetailedDTO;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import G2.SafeSpace.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import G2.SafeSpace.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    public UserController(UserService userService,
                          UserRepository userRepository,
                          UserContextService userContextService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userContextService = userContextService;
    }

    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

    @GetMapping("/users/me")
    public ResponseEntity<UserDetailedDTO> getMe() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetailedDTO detailed = userService.generateUserDetailedDTO(optionalUser.get());
        if (detailed != null) {
            return ResponseEntity.ok(detailed);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    //get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<UserDTO> users = userService.findAllUsers();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userService.findUserById(id);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user, true);
            userDTO.setUserData(userService.createUserData(user));
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //get user by username
    @GetMapping("/users/search")
    public ResponseEntity<UserDTO> getUserByName(@RequestParam String name) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = userRepository.findByUsername(name);
        if (user != null) {
            UserDTO userDTO = new UserDTO(user, true);
            userDTO.setUserData(userService.createUserData(user));
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //update user
    // REMOVED PATH VARIABLE FROM UPDATE USER
    @PutMapping("/users/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserDTO updatedUser) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDTO response = userService.updateUser(updatedUser, optionalUser.get());
        if (response == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok(response);
        }
    }

    // REMOVED PATH VARIABLE FROM DELETE USER
    @DeleteMapping("/users/delete")
    public ResponseEntity<User> deleteUser() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean deletedUserOptional = userService.deleteUser();
        if (deletedUserOptional) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/users/friends/{friend_id}")
    public ResponseEntity<String> addFriend(@PathVariable int friend_id) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User friend = userService.findUserById(friend_id);
        if (friend != null) {
            if (userService.areFriends(optionalUser.get(), friend)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Users are already friends.");
            }
            if (userService.hasSentFriendRequest(optionalUser.get(), friend)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Friend request has already been sent.");
            }

            Optional<User> updatedUserOptional = userService.addFriend(optionalUser.get(), friend);
            if (updatedUserOptional.isPresent()) {
                return ResponseEntity.ok().body("Friend request has been sent.");
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/users/friends/{friend_id}")
    public ResponseEntity<String> removeFriend(@PathVariable int friend_id) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User currentUser = optionalUser.get();
        User friend = userService.findUserById(friend_id);
        if (friend == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend with given id does not exist.");
        }

        // Check if users are friends
        if (userService.areFriends(currentUser, friend)) {
            // Users are friends, so remove the friendship
            Optional<User> updatedUserOptional = userService.removeFriend(currentUser, friend);
            if (updatedUserOptional.isPresent()) {
                return ResponseEntity.ok().body("Friend has been removed.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove friend.");
        }

        // Check if a friend request was sent but not yet accepted
        if (userService.hasSentFriendRequest(currentUser, friend)) {
            Optional<User> updatedUserOptional = userService.removeFriend(currentUser, friend);
            if (updatedUserOptional.isPresent()) {
                return ResponseEntity.ok().body("Friend request has been removed.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to remove friend request.");
        }

        // If neither condition is met
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("No pending friend request or friendship exists.");
    }

    @GetMapping("/users/friends")
    public ResponseEntity<List<Integer>> getFriends() {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get the authenticated user's friends
        List<Integer> friendIds = userService.getFriends(optionalUser.get());
        if (friendIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(friendIds);
    }
}
