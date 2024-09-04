package G2.SafeSpace.controller;

import G2.SafeSpace.entity.User;
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
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            userOptional.get().setPassword(null);
            return ResponseEntity.ok(userOptional.get());
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
}
