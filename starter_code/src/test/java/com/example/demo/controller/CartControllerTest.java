package com.example.demo.controller;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart() {
        // Create a test user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        Cart cart = new Cart();
        user.setCart(cart);

        // Create a test item
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(BigDecimal.valueOf(10.0));

        // Create a test request
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testuser");
        request.setItemId(1L);
        request.setQuantity(3);

        // Mock repository methods
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Make the API call
        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Cart responseCart = responseEntity.getBody();
        assertNotNull(responseCart);
        assertEquals(3, responseCart.getItems().size());
    }

    @Test
    public void testRemoveFromCart() {
        // Create a test user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        Cart cart = new Cart();
        user.setCart(cart);

        // Create a test item
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(BigDecimal.valueOf(10.0));

        // Add the item to the cart
        cart.addItem(item);

        // Create a test request
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testuser");
        request.setItemId(1L);
        request.setQuantity(1);

        // Mock repository methods
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Make the API call
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Cart responseCart = responseEntity.getBody();
        assertNotNull(responseCart);
        assertEquals(0, responseCart.getItems().size());
    }
}

