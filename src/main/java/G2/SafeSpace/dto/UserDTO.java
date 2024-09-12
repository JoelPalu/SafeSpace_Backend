package G2.SafeSpace.dto;

public class UserDTO {

    private int id;
    private String username;
    private String bio;
    private String profilePictureID;
    private String jwt;

    public UserDTO() {}

    public UserDTO(int id, String username,String bio, String profilePictureID, String jwt) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.profilePictureID = profilePictureID;
        this.jwt = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
