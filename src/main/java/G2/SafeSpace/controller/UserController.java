package G2.SafeSpace.controller;

import G2.SafeSpace.entity.User;
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        for (User user : users) {
            user.setPassword(null);
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
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        Optional<User> updatedUserOptional = userService.updateUser(id, updatedUser);
        if (updatedUserOptional.isPresent()) {
            return ResponseEntity.ok(updatedUserOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        boolean deletedUserOptional = userService.deleteUser(id);
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
