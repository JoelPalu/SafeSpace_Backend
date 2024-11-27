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

/**
 * Service for handling user-related operations such as retrieving, updating, and deleting users,
 * managing friendships, and liking posts.
 * <p>
 * This service interacts with the `UserRepository`, `MessageService`, and `CommentRepository`
 * to manage user data, friendships, likes, and events.
 * </p>
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageService messageService;
    private final CommentRepository commentRepository;

    /**
     * Constructor to initialize the UserService with dependencies.
     *
     * @param userRepository      The repository for accessing user data.
     * @param userContextService  The service for accessing the current user context.
     * @param passwordEncoder     The password encoder for encoding user passwords.
     * @param jwtService          The service for generating JWT tokens.
     * @param eventPublisher      The publisher for application events.
     * @param messageService      The service for handling messages.
     * @param commentRepository   The repository for accessing comments.
     */
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

    /**
     * Retrieves all users in the system, excluding sensitive data and including necessary user details.
     *
     * @return A list of {@link UserDTO} representing all users.
     */
    public List<UserDTO> findAllUsers() {
        try {
            List<User> rawUsers = userRepository.findAll();
            List<UserDTO> userDTOS = new ArrayList<>();
            for (User rawUser : rawUsers) {
                UserDTO userDTO = new UserDTO(rawUser, true);
                userDTO.setUserData(createUserData(rawUser));
                userDTOS.add(userDTO);
            }
            return userDTOS;
        } catch (RuntimeException e) {
            throw new RuntimeException("Could not find all users " + e.getMessage());
        }
    }

    /**
     * Checks if a username is available (i.e., not already taken by another user).
     *
     * @param username The username to check.
     * @return {@code true} if the username is available, {@code false} otherwise.
     */
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

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user with the specified ID, or {@code null} if not found.
     */
    public User findUserById(int id) {
        try {
            Optional<User> user = userRepository.findById(id);
            return user.orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find user by id" + id + " " + e.getMessage());
        }
    }

    /**
     * Updates the details of an existing user based on the provided {@link UpdateUserDTO}.
     *
     * @param updatedUser  The DTO containing updated user details.
     * @param existingUser The existing user entity to update.
     * @return The updated {@link UserDTO}, or {@code null} if the username is not available.
     */
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

    /**
     * Deletes the current authenticated user from the system.
     *
     * @return {@code true} if the user was successfully deleted, {@code false} if no authenticated user was found.
     */
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

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username, or {@code null} if not found.
     */
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

    /**
     * Generates a detailed DTO for a user, including their posts, liked posts, comments, and conversations.
     *
     * @param user The user for whom the detailed DTO is to be generated.
     * @return A {@link UserDetailedDTO} containing all relevant information about the user.
     */
    public UserDetailedDTO generateUserDetailedDTO(User user) {
        UserDetailedDTO userDetailedDTO = new UserDetailedDTO();
        UserDTO userDTO = new UserDTO(user, false);
        userDetailedDTO.setUser(userDTO);
        userDetailedDTO.setPosts(user.getPosts().stream().map(PostDTO::new).collect(Collectors.toList()));
        userDetailedDTO.setLikedPosts(user.getLikedPosts().stream().map(PostDTO::new).collect(Collectors.toList()));
        userDTO.setUserData(createUserData(user));
        userDetailedDTO.setConversations(messageService.getConversations(user));
        List<Comment> comments = commentRepository.findAllByUser(user);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment userComment : comments) {
            commentDTOS.add(new CommentDTO(userComment));
        }
        userDetailedDTO.setComments(commentDTOS);
        return userDetailedDTO;
    }

    /**
     * Creates user data containing information about the user's followers, following, and friends.
     *
     * @param user The user whose data is to be created.
     * @return A {@link UserData} object containing the user's relationship data.
     */
    public UserData createUserData(User user) {
        UserData userData = new UserData();

        // separate different friendship statuses
        ArrayList<UserDTO> following = new ArrayList<>();
        ArrayList<UserDTO> followers = new ArrayList<>();
        ArrayList<UserDTO> friends = new ArrayList<>();
        List<User> followersList = userRepository.findFollowers(user.getUserID());
        for (User friend : user.getFriends()) {
            //check if both users are following each other
            if (friend.getFriends().contains(user)) {
                userData.addFriendCount();
                friends.add(new UserDTO(friend, false));
            } else {
                // otherwise current user is just following
                userData.addFollowingCount();
                following.add(new UserDTO(friend, false));
            }
        }
        for (User follower : followersList) {
            // if other users have added current user but not vise versa
            userData.addFollowerCount();
            followers.add(new UserDTO(follower, false));
        }
        userData.setFollowing(following);
        userData.setFollowers(followers);
        userData.setFriends(friends);
        return userData;
    }

    /**
     * Adds a friend to the user's friend list. If the users are not already friends, a friend request is sent.
     *
     * @param user  The user who is adding a friend.
     * @param friend The user to be added as a friend.
     * @return An {@link Optional} containing the updated user if successful, or an empty {@link Optional} if unsuccessful.
     */
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

    /**
     * Adds a like to a post from the specified user.
     *
     * @param user The user who is adding a like.
     * @param post The post to which the like is being added.
     * @return {@code true} if the like was successfully added, {@code false} otherwise.
     */
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

    /**
     * Removes a like from a post for the specified user.
     *
     * @param user The user who is removing the like.
     * @param post The post from which the like is being removed.
     * @return {@code true} if the like was successfully removed, {@code false} otherwise.
     */
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

    /**
     * Checks if the sender has already sent a friend request to the receiver.
     *
     * @param sender  The user who may have sent the friend request.
     * @param receiver The user who may have received the friend request.
     * @return {@code true} if the sender has sent a friend request to the receiver, {@code false} otherwise.
     */
    public boolean hasSentFriendRequest(User sender, User receiver) {
        return sender.getFriends().contains(receiver);
    }

    /**
     * Checks if two users are friends.
     *
     * @param sender  The first user.
     * @param receiver The second user.
     * @return {@code true} if both users are friends, {@code false} otherwise.
     */
    public boolean areFriends(User sender, User receiver) {
        return sender.getFriends().contains(receiver) && receiver.getFriends().contains(sender);
    }

    /**
     * Removes a friend from the user's friend list.
     * <p>
     * This method checks if the specified user and friend are currently friends. If they are, it removes the friend
     * from the user's friend list and updates the user in the repository. An event is published to indicate whether
     * the friend was removed or if the friend request was removed.
     * </p>
     *
     * @param user The user from whose friend list the friend should be removed.
     * @param friend The user to be removed from the friend's list of the specified user.
     * @return An Optional containing the updated user if the friend was successfully removed,
     *         or an empty Optional if the removal was unsuccessful.
     * @throws RuntimeException if an error occurs while attempting to remove the friend.
     */
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

    /**
     * Retrieves a list of friend IDs for the specified user.
     * <p>
     * This method returns a list of user IDs for all the friends of the specified user. It collects the IDs of
     * all users in the user's friends list.
     * </p>
     *
     * @param user The user whose friends' IDs should be retrieved.
     * @return A list of integers representing the IDs of the user's friends.
     * @throws RuntimeException if an error occurs while retrieving the friend IDs.
     * */
    public List<Integer> getFriends(User user) {
        try {
            return user.getFriends().stream()
                    .map(User::getUserID)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get friend IDs " + e.getMessage());
        }
    }

    /**
     * Generates additional claims for a user to be included in their JWT token.
     * <p>
     * This method creates a map containing extra claims, including the user's username and user ID, which will be
     * included in the JWT token when it is generated.
     * </p>
     *
     * @param user The user for whom the additional claims should be generated.
     * @return A map containing the additional claims for the user.
     */
    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsername());
        extraClaims.put("id", user.getUserID());
        return extraClaims;
    }
}
