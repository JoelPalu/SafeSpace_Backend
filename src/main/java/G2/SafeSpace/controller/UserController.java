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

/**
 * Controller class responsible for managing user-related operations,
 * including retrieving, updating, and deleting users, as well as managing friendships.
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    /**
     * Constructs a new instance of UserController.
     *
     * @param userService        the UserService to be injected
     * @param userRepository     the UserRepository to be injected
     * @param userContextService the UserContextService to be injected
     */
    public UserController(UserService userService,
                          UserRepository userRepository,
                          UserContextService userContextService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userContextService = userContextService;
    }

    /**
     * Helper method to get the currently authenticated user.
     *
     * @return an {@link Optional} containing the {@link User} if authenticated, otherwise empty
     */
    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

    /**
     * Retrieves the authenticated user's detailed information.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If an error occurs while generating the user details, an Internal Server Error status is returned.
     *
     * @return a ResponseEntity containing the user details if successful, or an error status otherwise
     */
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


    /**
     * Retrieves a list of all users.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If no users are found, a No Content status is returned.
     *
     * @return a ResponseEntity containing a list of UserDTO objects if users are found, or an error status otherwise
     */
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

    /**
     * Retrieves a user by their ID.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the user is not found, a Not Found status is returned.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the UserDTO if found, or a Not Found status otherwise
     */
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

    /**
     * Retrieves a user by their username.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the user is not found, a Not Found status is returned.
     *
     * @param name the username of the user to retrieve
     * @return a ResponseEntity containing the UserDTO if found, or a Not Found status otherwise
     */
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

    /**
     * Updates the current user's details.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the update operation fails due to conflicting data, a Conflict status is returned.
     *
     * @param updatedUser the updated user details
     * @return a ResponseEntity containing the updated UserDTO if successful, or a Conflict status otherwise
     */
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

    /**
     * Deletes the current user's account.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the user is successfully deleted, an OK status is returned.
     * If the deletion fails, a Not Found status is returned.
     *
     * @return a ResponseEntity indicating the result of the delete operation
     */
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

    /**
     * Sends a friend request to a user with the specified ID.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the users are already friends or a request has already been sent, a Conflict status is returned.
     *
     * @param friend_id the ID of the user to send a friend request to
     * @return a ResponseEntity indicating the result of the friend request operation
     */
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

    /**
     * Removes a friend from the current user's friend list.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the users are not friends or no friend request exists, a Conflict status is returned.
     *
     * @param friend_id the ID of the friend to remove
     * @return a ResponseEntity indicating the result of the removal operation
     */
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

    /**
     * Retrieves the list of friends for the currently authenticated user.
     * If the user is not authenticated, an Unauthorized status is returned.
     * If the user has no friends, a Not Found status is returned.
     * Otherwise, the list of friend IDs is returned with an OK status.
     *
     * @return a ResponseEntity containing the list of friend IDs or an appropriate status:
     *         - OK (200) with the list of friend IDs if the user has friends
     *         - Unauthorized (401) if the user is not authenticated
     *         - Not Found (404) if the user has no friends
     */
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
