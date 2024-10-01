package G2.SafeSpace;

import G2.SafeSpace.controller.UserController;
import G2.SafeSpace.dto.UserDTO;
import G2.SafeSpace.dto.UserDetailedDTO;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.repository.UserRepository;
import G2.SafeSpace.service.UserService;
import G2.SafeSpace.service.UserContextService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserContextService userContextService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        User user2 = new User();

        when(userContextService.getCurrentUser()).thenReturn(Optional.of(user));
        when(userService.findAllUsers()).thenReturn(List.of(new UserDTO(user, true), new UserDTO(user2, false)));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        when(userContextService.getCurrentUser()).thenReturn(Optional.of(user));
        when(userService.findUserById(1)).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getUserById(1);

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUserID(), response.getBody().getId());
    }

    @Test
    public void testMe() {
        User user = new User();
        when(userContextService.getCurrentUser()).thenReturn(Optional.of(user));
        when(userService.generateUserDetailedDTO(user)).thenReturn(new UserDetailedDTO());

        ResponseEntity<UserDetailedDTO> response = userController.getMe();

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setUsername("username");
        when(userContextService.getCurrentUser()).thenReturn(Optional.of(user));
        when(userService.findUserByUsername("username")).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getUserByName("username");

        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUserID(), response.getBody().getId());
    }
}