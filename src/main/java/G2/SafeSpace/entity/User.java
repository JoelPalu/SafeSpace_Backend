package G2.SafeSpace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column
    private String Username;

    @Column
    private String password;

    @Column
    private String Bio;

    @Column
    private String dateOfCreation;

    @Column
    private int ProfilePictureID;

    public User() {}

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return Bio;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public int getProfilePictureID() {
        return ProfilePictureID;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBio(String bio) {
        this.Bio = bio;
    }

    public void setProfilePictureID(int profilePictureID) {
        this.ProfilePictureID = profilePictureID;
    }
}
