package G2.SafeSpace.controller;

import G2.SafeSpace.dto.UpdateUserResponse;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.service.UserContextService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import G2.SafeSpace.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //get all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(users);
    }

    //get user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.findUserById(id);
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //get user by username
    @GetMapping("/users/search")
    public ResponseEntity<User> getUserByName(@RequestParam String name) {
        User user = userService.findUserByUsername(name);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //update user
    // REMOVED PATH VARIABLE FROM UPDATE USER
    @PutMapping("/users/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody User updatedUser) {
        UpdateUserResponse response = userService.updateUser(updatedUser);
        if (response != null) {
            if (response.isNameTaken()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            UserDTO user = response.getUser();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // REMOVED PATH VARIABLE FROM DELETE USER
    @DeleteMapping("/users/delete")
    public ResponseEntity<User> deleteUser() {
        boolean deletedUserOptional = userService.deleteUser();
        if (deletedUserOptional) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/users/friends/{friend_id}")
    public ResponseEntity<User> addFriend(@PathVariable int friend_id) {
        // Retrieve the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Find the authenticated user by username
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User friend = userService.findUserById(friend_id);
        if (friend == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<User> updatedUserOptional = userService.addFriend(authenticatedUser, friend);
        if (updatedUserOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/friends/{friend_id}")
    public ResponseEntity<User> removeFriend(@PathVariable int friend_id) {
        // Retrieve the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Find the authenticated user by username
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User friend = userService.findUserById(friend_id);
        if (friend == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<User> updatedUserOptional = userService.removeFriend(authenticatedUser, friend);
        if (updatedUserOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/friends")
    public ResponseEntity<List<Integer>> getFriends() {

        // Retrieve the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();
        System.out.println("Authenticated user: " + authenticatedUsername);

        // Find the authenticated user by username
        User authenticatedUser = userService.findUserByUsername(authenticatedUsername);
        if (authenticatedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Get the authenticated user's friends
        List<Integer> friendIds = userService.getFriends(authenticatedUser);
        if (friendIds.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(friendIds);
    }
}
