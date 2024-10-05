package G2.SafeSpace.service;

import G2.SafeSpace.config.JwtService;
import G2.SafeSpace.dto.*;
import G2.SafeSpace.entity.Comment;
import G2.SafeSpace.entity.Post;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.event.FriendrequestEvent;
import G2.SafeSpace.event.LikeEvent;
import G2.SafeSpace.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import G2.SafeSpace.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageService messageService;
    private final CommentRepository commentRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserContextService userContextService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       ApplicationEventPublisher eventPublisher,
                       MessageService messageService,
                       CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.userContextService = userContextService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
        this.messageService = messageService;
        this.commentRepository = commentRepository;
    }

    public List<UserDTO> findAllUsers() {
        try {
            List<User> rawUsers = userRepository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            for (User rawUser : rawUsers) {
                UserDTO userDTO = new UserDTO(rawUser, true);
                userDTOS.add(userDTO);
            }
            return userDTOS;
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not find all users " + e.getMessage());
        }
    }

    public boolean isUsernameAvailable(String username) {
        List<User> users = userRepository.findAll();
        boolean available = true;
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    available = false;
                    break;
                }
            }
        }
        return available;
    }



    public User findUserById(int id) {
        try {
            Optional<User> user = userRepository.findById(id);
            return user.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id" + id + " " + e.getMessage());
        }
    }

    public UserDTO updateUser(UpdateUserDTO updatedUser, User existingUser) {

        String username = updatedUser.getUsername();
        String password = updatedUser.getPassword();
        String bio = updatedUser.getBio();
        String profilepictureID = updatedUser.getProfilePictureID();

        if (!existingUser.getUsername().equals(username) && username != null && !username.trim().isEmpty()) {
            if (isUsernameAvailable(username)) {
                existingUser.setUsername(username.trim());
            } else {
                return null;
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

        UserDTO userDTO = new UserDTO(savedUser, false);
        userDTO.setJwt(jwtService.generateToken(savedUser, generateExtraClaims(savedUser)));

        return userDTO;
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

    public UserDetailedDTO generateUserDetailedDTO(User user) {
        UserDetailedDTO userDetailedDTO = new UserDetailedDTO();
        UserDTO userDTO = new UserDTO(user, false);
        userDetailedDTO.setUser(userDTO);
        userDetailedDTO.setPosts(user.getPosts().stream().map(PostDTO::new).collect(Collectors.toList()));
        userDetailedDTO.setLikedPosts(user.getLikedPosts().stream().map(PostDTO::new).collect(Collectors.toList()));
        ArrayList<UserDTO> following = new ArrayList<>();
        ArrayList<UserDTO> followers = new ArrayList<>();
        ArrayList<UserDTO> friends = new ArrayList<>();
        List<User> followersList = userRepository.findFollowers(user.getUserID());
        for (User friend : user.getFriends()) {
            //check if both users are following each other
            if (friend.getFriends().contains(user)) {
                friends.add(new UserDTO(friend, false));
            } else {
                // otherwise current user is just following
                following.add(new UserDTO(friend, false));
            }
        }
        for (User follower : followersList) {
            // if other users have added current user but not vise versa
            followers.add(new UserDTO(follower, false));
        }
        userDetailedDTO.setFollowing(following);
        userDetailedDTO.setFollowers(followers);
        userDetailedDTO.setFriends(friends);
        userDetailedDTO.setMessages(messageService.getMessages(user));
        List<Comment> comments = commentRepository.findAllByUser(user);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment userComment : comments) {
            commentDTOS.add(new CommentDTO(userComment));
        }
        userDetailedDTO.setComments(commentDTOS);
        return userDetailedDTO;
    }

    public Optional<User> addFriend(User user, User friend) {
        try {
            user.addFriends(friend);
            User savedUser = userRepository.save(user);

            if (savedUser.getFriends().contains(friend)) {
                String eventType;
                if (areFriends(savedUser, friend)) {
                    eventType = "new_friend";
                } else {
                    eventType = "friend_request";
                }
                eventPublisher.publishEvent(new FriendrequestEvent(new FriendshipDTO(savedUser.getUserID(), friend.getUserID(), eventType)));
                return Optional.of(savedUser);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add friend " + e.getMessage());
        }
    }

    public boolean likeAdded(User user, Post post) {
        try {
            user.addLikedPost(post);
            User savedUser = userRepository.save(user);

            if (savedUser.getLikedPosts().contains(post)) {
                eventPublisher.publishEvent(new LikeEvent(new LikeDTO(user.getUserID(), post.getPostID(), "like_added")));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean likeRemoved(User user, Post post) {
        try {
            user.removeLikedPost(post);
            User savedUser = userRepository.save(user);

            if (!savedUser.getLikedPosts().contains(post)) {
                eventPublisher.publishEvent(new LikeEvent(new LikeDTO(user.getUserID(), post.getPostID(), "like_removed")));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasSentFriendRequest(User sender, User receiver) {
        return sender.getFriends().contains(receiver);
    }

    public boolean areFriends(User sender, User receiver) {
        return sender.getFriends().contains(receiver) && receiver.getFriends().contains(sender);
    }


    public Optional<User> removeFriend(User user, User friend) {
        try {
            boolean wereFriends = areFriends(user, friend);

            user.removeFriends(friend);
            User savedUser = userRepository.save(user);

            if (!savedUser.getFriends().contains(friend)) {
                String eventType = wereFriends ? "friend_removed" : "friend_request_removed";
                eventPublisher.publishEvent(
                        new FriendrequestEvent(
                                new FriendshipDTO(
                                        savedUser.getUserID(),
                                        friend.getUserID(),
                                        eventType
                                )
                        )
                );
                return Optional.of(savedUser);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove friend: " + e.getMessage(), e);
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
