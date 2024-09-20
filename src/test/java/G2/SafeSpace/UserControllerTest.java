package G2.SafeSpace;

import G2.SafeSpace.controller.UserController;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.service.UserService;
import G2.SafeSpace.service.UserContextService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserContextService userContextService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        when(userContextService.getCurrentUser()).thenReturn(Optional.of(user));
        when(userService.findAllUsers()).thenReturn(List.of(new UserDTO(user, true)));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        when(userContextService.getCurrentUser()).thenReturn(Optional.of(user));
        when(userService.findUserById(1)).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getUserById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUserID(), response.getBody().getId());
    }

}