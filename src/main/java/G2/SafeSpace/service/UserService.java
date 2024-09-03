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

                String username = updatedUser.getUsername();
                String password = updatedUser.getPassword();
                String bio = updatedUser.getBio();
                String profilepictureID = updatedUser.getProfilePictureID();

                User existingUser = existingUserOptional.get();

                if (!existingUser.getUsername().equals(username) && !username.trim().isEmpty()) {
                    existingUser.setUsername(username);
                }
                if (!existingUser.getPassword().equals(password) && !password.trim().isEmpty()) {
                    existingUser.setPassword(password);
                }

                existingUser.setBio(bio);


                if (!existingUser.getProfilePictureID().equals(profilepictureID)) {
                    existingUser.setProfilePictureID(profilepictureID);
                }

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

    public void save(User user) {
        userRepository.save(user);
    }
}
