package com.example.demo.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setup() {
        userController = new UserController();
        ReflectionTestUtils.setField(userController, "userRepository", userRepository);
        ReflectionTestUtils.setField(userController, "cartRepository", cartRepository);
        ReflectionTestUtils.setField(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        ResponseEntity<User> responseEntity = userController.findById(1L);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User responseUser = responseEntity.getBody();
        assertNotNull(responseUser);
        assertEquals("testUser", responseUser.getUsername());
    }

    @Test
    public void testFindByUserName() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.findByUserName("testUser");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User responseUser = responseEntity.getBody();
        assertNotNull(responseUser);
        assertEquals("testUser", responseUser.getUsername());
    }

    @Test
    public void testCreateUser_Success() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        Cart cart = new Cart();
        when(cartRepository.save(cart)).thenReturn(cart);

        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setCart(cart);

        when(bCryptPasswordEncoder.encode(createUserRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User responseUser = responseEntity.getBody();
        assertNotNull(responseUser);
        assertEquals("testUser", responseUser.getUsername());
    }

    @Test
    public void testCreateUser_InvalidPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("short");
        createUserRequest.setConfirmPassword("short");

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        User responseUser = responseEntity.getBody();
        assertNull(responseUser);
    }

    @Test
    public void testCreateUser_PasswordMismatch() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("testUser");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("mismatch");

        ResponseEntity<User> responseEntity = userController.createUser(createUserRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        User responseUser = responseEntity.getBody();
        assertNull(responseUser);
    }
}

