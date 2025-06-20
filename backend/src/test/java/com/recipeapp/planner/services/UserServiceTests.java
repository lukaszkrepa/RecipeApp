package com.recipeapp.planner.services;

import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.repositories.UserRepository;
import com.recipeapp.planner.services.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("<= UserServiceTests =>")
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("createUser")
    @Order(1)
    public void createUserTest() {
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setUserId(UUID.randomUUID());
            return user;
        });

        UserEntity savedUser = userService.createUser("user");

        assertAll(
                () -> assertNotNull(savedUser),
                () -> assertEquals("user", savedUser.getUsername()),
                () -> assertNotNull(savedUser.getUserId())
        );
    }

    @Test
    @DisplayName("create user with empty name")
    @Order(2)
    public void createUserWithEmptyNameTest() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(""));
    }

    @Test
    @DisplayName("create user with null name")
    @Order(3)
    public void createUserWithNullNameTest() {
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(null));
    }

    @Test
    @DisplayName("create user with existing name")
    @Order(4)
    public void createUserWithExistingNameTest() {
        when(userRepository.existsByUsername("user")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser("user"));
    }

    @Test
    @DisplayName("get user by id")
    @Order(5)
    public void getUserByIdTest() {
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setUsername("user");

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserById(user.getUserId());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("user", result.getUsername()),
                () -> assertEquals(user.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("get user by id with non-existing id")
    @Order(6)
    public void getUserByIdWithNonExistingIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("get user by id with null id")
    @Order(7)
    public void getUserByIdWithNullIdTest() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(null));
    }

    @Test
    @DisplayName("get user by username")
    @Order(8)
    public void getUserByUsernameTest() {
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setUsername("user");

        when(userRepository.findUserEntitieByUsername("user")).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserByUsername("user");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("user", result.getUsername()),
                () -> assertEquals(user.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("get user by username with non-existing username")
    @Order(9)
    public void getUserByUsernameWithNonExistingUsernameTest() {
        when(userRepository.findUserEntitieByUsername("non-existing")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername("non-existing"));
    }

    @Test
    @DisplayName("get user by username with null username")
    @Order(10)
    public void getUserByUsernameWithNullUsernameTest() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername(null));
    }

    @Test
    @DisplayName("get all users")
    @Order(11)
    public void getAllUsersTest() {
        List<UserEntity> users = List.of(
                new UserEntity(UUID.randomUUID(), "user"),
                new UserEntity(UUID.randomUUID(), "user2"),
                new UserEntity(UUID.randomUUID(), "user3")
        );
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    @DisplayName("get all users with no users")
    @Order(12)
    public void getAllUsersWithNoUsersTest() {
        when(userRepository.findAll()).thenReturn(List.of());
        List<UserEntity> userEntities = userService.getAllUsers();
        assertAll(
                () -> assertNotNull(userEntities),
                () -> assertEquals(0, userEntities.size())
        );
    }

    @Test
    @DisplayName("update user")
    @Order(13)
    public void updateUserTest() {
        UUID id = UUID.randomUUID();
        UserEntity existingUser = new UserEntity(id, "user");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity updatedUser = userService.updateUser(id, "newUser");

        assertAll(
                () -> assertNotNull(updatedUser),
                () -> assertEquals("newUser", updatedUser.getUsername()),
                () -> assertEquals(id, updatedUser.getUserId())
        );
    }

    @Test
    @DisplayName("update user with non-existing id")
    @Order(14)
    public void updateUserWithNonExistingIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(UUID.randomUUID(), "newUser"));
    }

    @Test
    @DisplayName("update user with null id")
    @Order(15)
    public void updateUserWithNullIdTest() {
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null, "newUser"));
    }

    @Test
    @DisplayName("update user with empty name")
    @Order(16)
    public void updateUserWithEmptyNameTest() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new UserEntity(id, "user")));
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(id, ""));
    }

    @Test
    @DisplayName("update user with null name")
    @Order(17)
    public void updateUserWithNullNameTest() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new UserEntity(id, "user")));
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(id, null));
    }

    @Test
    @DisplayName("update user with existing name")
    @Order(18)
    public void updateUserWithExistingNameTest() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new UserEntity(id, "user2")));
        when(userRepository.existsByUsername("user")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(id, "user"));
    }

    @Test
    @DisplayName("delete user")
    @Order(19)
    public void deleteUserTest() {
        UUID id = UUID.randomUUID();
        UserEntity user = new UserEntity(id, "user");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(id);

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("delete user with non-existing id")
    @Order(20)
    public void deleteUserWithNonExistingIdTest() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(UUID.randomUUID()));
    }

    @Test
    @DisplayName("delete user with null id")
    @Order(21)
    public void deleteUserWithNullIdTest() {
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(null));
    }
}
