package G2.SafeSpace.dto;

/**
 * Represents a request for authentication, containing the user's credentials.
 * This class is typically used for sending the username and password for authentication purposes.
 */
public class AuthenticationRequest {

    private String username;
    private String password;

    /**
     * Gets the username of the user.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password as a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
