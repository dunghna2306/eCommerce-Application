package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSubmitOrder() {
        // Create a test user
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        // Create a mock list of items
        List<Item> mockItems = new ArrayList<>();
        mockItems.add(new Item(1L, "Item 1", new BigDecimal("10.0"), "Description 1"));
        mockItems.add(new Item(2L, "Item 2", new BigDecimal("20.0"), "Description 2"));

        // Create a test cart for the user
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(mockItems);
        user.setCart(cart);

        // Mock repository method
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        // Make the API call
        ResponseEntity<UserOrder> responseEntity = orderController.submit("testUser");

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        UserOrder order = responseEntity.getBody();
        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(mockItems, order.getItems());
    }

    @Test
    public void testSubmitOrder_UserNotFound() {
        // Mock repository method for user not found
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(null);

        // Make the API call
        ResponseEntity<UserOrder> responseEntity = orderController.submit("nonExistingUser");

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetOrdersForUser() {
        // Create a test user
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        // Create a list of test orders
        List<UserOrder> orders = new ArrayList<>();
        UserOrder order1 = new UserOrder();
        order1.setId(1L);
        order1.setUser(user);
        orders.add(order1);
        UserOrder order2 = new UserOrder();
        order2.setId(2L);
        order2.setUser(user);
        orders.add(order2);

        // Mock repository method
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(orders);

        // Make the API call
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("testUser");

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<UserOrder> responseOrders = responseEntity.getBody();
        assertNotNull(responseOrders);
        assertEquals(2, responseOrders.size());
        assertEquals(user, responseOrders.get(0).getUser());
        assertEquals(user, responseOrders.get(1).getUser());
    }

    @Test
    public void testGetOrdersForUser_UserNotFound() {
        // Mock repository method for user not found
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(null);

        // Make the API call
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("nonExistingUser");

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}

