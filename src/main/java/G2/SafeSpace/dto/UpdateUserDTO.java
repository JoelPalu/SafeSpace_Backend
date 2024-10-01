package G2.SafeSpace.dto;

public class UpdateUserDTO {
    private String username;
    private String password;
    private String bio;
    private String profilePictureID;

    public UpdateUserDTO() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePictureID() {
        return profilePictureID;
    }

    public void setProfilePictureID(String profilePictureID) {
        this.profilePictureID = profilePictureID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
