package G2.SafeSpace.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) for encapsulating user relationship data.
 * This class holds information about the user's social interactions,
 * including counts and lists of followers, following users, and friends.
 */
public class UserData {

    private int followingCount;
    private int followersCount;
    private int friendsCount;
    private List<UserDTO> following;
    private List<UserDTO> followers;
    private List<UserDTO> friends;

    /**
     * Default constructor for UserData.
     */
    public UserData() {}

    /**
     * Gets the count of followers.
     *
     * @return the number of followers
     */
    public int getFollowersCount() {
        return followersCount;
    }

    /**
     * Sets the count of followers.
     *
     * @param followersCount the number of followers to set
     */
    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * Increments the count of followers by 1.
     */
    public void addFollowerCount() {
        followersCount++;
    }

    /**
     * Gets the count of users being followed.
     *
     * @return the number of users being followed
     */
    public int getFollowingCount() {
        return followingCount;
    }

    /**
     * Sets the count of users being followed.
     *
     * @param followingCount the number of users to set
     */
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    /**
     * Increments the count of users being followed by 1.
     */
    public void addFollowingCount() {
        followingCount++;
    }

    /**
     * Gets the count of friends.
     *
     * @return the number of friends
     */
    public int getFriendsCount() {
        return friendsCount;
    }

    /**
     * Sets the count of friends.
     *
     * @param friendsCount the number of friends to set
     */
    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    /**
     * Increments the count of friends by 1.
     */
    public void addFriendCount() {
        friendsCount++;
    }

    /**
     * Gets the list of followers.
     *
     * @return the list of followers as UserDTO objects
     */
    public List<UserDTO> getFollowers() {
        return followers;
    }

    /**
     * Sets the list of followers.
     *
     * @param followers the list of followers to set
     */
    public void setFollowers(List<UserDTO> followers) {
        this.followers = followers;
    }

    /**
     * Gets the list of friends.
     *
     * @return the list of friends as UserDTO objects
     */
    public List<UserDTO> getFriends() {
        return friends;
    }

    /**
     * Sets the list of friends.
     *
     * @param friends the list of friends to set
     */
    public void setFriends(List<UserDTO> friends) {
        this.friends = friends;
    }

    /**
     * Gets the list of users being followed.
     *
     * @return the list of following users as UserDTO objects
     */
    public List<UserDTO> getFollowing() {
        return following;
    }

    /**
     * Sets the list of users being followed.
     *
     * @param following the list of users to set
     */
    public void setFollowing(List<UserDTO> following) {
        this.following = following;
    }
}
