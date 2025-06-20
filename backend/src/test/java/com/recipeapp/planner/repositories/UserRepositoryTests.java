package com.recipeapp.planner.repositories;

import com.recipeapp.planner.domain.entities.UserEntity;
import com.recipeapp.planner.resolvers.UserParameterResolver;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@DisplayName("<= UserRepositoryTests =>")
@ExtendWith(UserParameterResolver.class)
class UserRepositoryTests {

    private UserEntity user1;
    private UserEntity user2;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp(Map<String, UserEntity> users) {
        user1 = users.get("user1");
        user2 = users.get("user2");
    }

    @Test
    @Order(1)
    @DisplayName("Test that a user can be saved")
    void saveUserTest() {
        UserEntity userEntity = user1;
        UserEntity savedUser = userRepository.save(userEntity);

        assertAll(
                () -> assertNotNull(savedUser.getUserId(), () -> "User doesn't have Id set"),
                () -> assertEquals("TestUser", savedUser.getUsername(), () -> "Expected username to be 'TestUser' but was " + savedUser.getUsername() + " instead")
        );
    }

    @Test
    @Order(2)
    @DisplayName("Test that a user can be found using the userId")
    void findUserByIdTest() {
        UserEntity userEntity = user1;
        UserEntity savedUser = userRepository.save(userEntity);

        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getUserId());

        assertTrue(foundUser.isPresent(), () -> "User was not found in the database");

        assertAll(
                () -> assertEquals(savedUser.getUserId(), foundUser.get().getUserId(),  () -> "Expected userId to be " + savedUser.getUserId() + " but was " + foundUser.get().getUserId() + " instead"),
                () -> assertEquals("TestUser", foundUser.get().getUsername(), () -> "Expected username to be 'TestUser' but was " + foundUser.get().getUsername() + " instead")
        );
    }

    @Test
    @Order(3)
    @DisplayName("Test that a user can be found using the username")
    void findUserByUsernameTest() {
        UserEntity userEntity = user1;
        userRepository.save(userEntity);

        Optional<UserEntity> foundUser = userRepository.findUserEntitieByUsername("TestUser");

        assertTrue(foundUser.isPresent(), () -> "User was not found in the database");
        assertEquals("TestUser", foundUser.get().getUsername(), () -> "Expected username to be 'TestUser' but was " + foundUser.get().getUsername() + " instead");
    }

    @Test
    @Order(4)
    @DisplayName("Test that a user can be updated")
    void updateUserTest() {
        UserEntity userEntity = user1;
        UserEntity savedUser = userRepository.save(userEntity);

        savedUser.setUsername("UpdatedUser");
        UserEntity updatedUser = userRepository.save(savedUser);

        assertEquals("UpdatedUser", updatedUser.getUsername(), () -> "Expected username to be UpdatedUser but was " + updatedUser.getUsername() + " instead");
    }

    @Test
    @Order(5)
    @DisplayName("Test that a user can be deleted")
    void deleteUserTest() {
        UserEntity userEntity = user1;
        UserEntity savedUser = userRepository.save(userEntity);

        userRepository.delete(savedUser);

        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getUserId());
        assertTrue(foundUser.isEmpty(), () -> "User was found in the database after deletion" + foundUser.get());
    }

    @Test
    @Order(6)
    @DisplayName("Test that multiple users can be found")
    void findMultipleUsersTest() {
        userRepository.save(user1);
        userRepository.save(user2);

        assertEquals(2, userRepository.findAll().size(), () -> "Expected 2 users to be found but found " + userRepository.findAll().size() + " instead");
    }
}
