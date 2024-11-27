package G2.SafeSpace.dto;

/**
 * Data Transfer Object (DTO) for updating user details.
 * This class contains the fields that can be updated for a user, such as
 * username, password, bio, and profile picture ID.
 */
public class UpdateUserDTO {

    private String username;
    private String password;
    private String bio;
    private String profilePictureID;

    /**
     * Default constructor for UpdateUserDTO.
     */
    public UpdateUserDTO() {}

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the bio of the user.
     *
     * @return the bio of the user
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the bio of the user.
     *
     * @param bio the bio to set
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the profile picture ID of the user.
     *
     * @return the profile picture ID of the user
     */
    public String getProfilePictureID() {
        return profilePictureID;
    }

    /**
     * Sets the profile picture ID of the user.
     *
     * @param profilePictureID the profile picture ID to set
     */
    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
