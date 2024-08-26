package G2.SafeSpace.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String bio;

    @Column(nullable = false)
    private String dateOfCreation;

    @Column
    private int profilePictureID;

    public User() {}

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public int getProfilePictureID() {
        return profilePictureID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePictureID(int profilePictureID) {
        this.profilePictureID = profilePictureID;
    }
}
