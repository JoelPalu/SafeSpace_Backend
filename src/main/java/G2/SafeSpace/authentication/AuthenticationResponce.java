package G2.SafeSpace.authentication;

import G2.SafeSpace.entity.User;

public class AuthenticationResponce {

    private String jwt;
    private User user;

    public AuthenticationResponce(String jwt, User user) {
        this.jwt = jwt;
        this.user = user;
        this.user.setPassword(null);

    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
