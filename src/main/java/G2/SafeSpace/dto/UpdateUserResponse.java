package G2.SafeSpace.dto;

import G2.SafeSpace.entity.User;

public class UpdateUserResponse {

    private boolean nameTaken = false;
    private boolean success = false;
    private UserDTO userDTO;


    public UpdateUserResponse(boolean nameTaken, boolean success, UserDTO userDTO) {
        this.nameTaken = nameTaken;
        this.success = success;
        this.userDTO = userDTO;
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

    public UserDTO getUser() {
        return this.userDTO;
    }

    public void setUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
