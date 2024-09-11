package G2.SafeSpace.dto;

import G2.SafeSpace.entity.User;

public class UpdateUserResponse {

    private boolean nameTaken = false;
    private boolean success = false;
    private User user;


    public UpdateUserResponse(boolean nameTaken, boolean success, User user) {
        this.nameTaken = nameTaken;
        this.success = success;
        this.user = user;
    }

    public boolean isNameTaken() {
        return this.nameTaken;
    }

    public void setNameTaken(boolean nameTaken) {
        this.nameTaken = nameTaken;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
