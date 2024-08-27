package G2.SafeSpace.authentication;

public class AuthenticationRequest {

    private String username;
    private String password;

    public String getUsername() {
        System.out.println("AuthRequets");
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
