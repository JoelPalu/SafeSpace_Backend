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
            throw new RuntimeException("User creation failed" + e.getMessage());
        }
    }

    public List<User> findAllUsers() {
        try {
            System.out.println("service" + userRepository.findAll());
            return userRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not find all users " + e.getMessage());
        }
    }

    public Optional<User> findUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id" + id + " " + e.getMessage());
        }
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
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

    public boolean deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user " + id + " " + e.getMessage());
        }
    }
}
