package G2.SafeSpace.service;

import G2.SafeSpace.entity.User;
import org.springframework.stereotype.Service;
import G2.SafeSpace.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("User creation failed " + e.getMessage());
        }
    }

    public List<User> findAllUsers() {
        try {
            return userRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not find all users " + e.getMessage());
        }
    }

    public Optional<User> findUserById(int id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id" + id + " " + e.getMessage());
        }
    }

    public Optional<User> updateUser(int id, User updatedUser) {
        try {
            Optional<User> existingUserOptional = userRepository.findById(id);
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setPassword(updatedUser.getPassword());
                existingUser.setBio(updatedUser.getBio());
                existingUser.setProfilePictureID(updatedUser.getProfilePictureID());
                User savedUser = userRepository.save(existingUser);
                return Optional.of(savedUser);
            } else return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user " + id + " " + e.getMessage());
        }
    }

    public boolean deleteUser(int id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                userRepository.deleteById(id);
                return true;
            } else return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user " + id + " " + e.getMessage());
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
}
