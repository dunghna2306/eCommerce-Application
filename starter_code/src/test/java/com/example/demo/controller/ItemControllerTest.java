package com.example.demo.controller;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItems() {
        // Create a list of test items
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Item 1", new BigDecimal("10.0"), "Description 1"));
        items.add(new Item(2L, "Item 2", new BigDecimal("20.0"), "Description 2"));

        // Mock repository method
        when(itemRepository.findAll()).thenReturn(items);

        // Make the API call
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Item> responseItems = responseEntity.getBody();
        assertNotNull(responseItems);
        assertEquals(2, responseItems.size());
    }

    @Test
    public void testGetItemById() {
        // Create a test item
        Item item = new Item(1L, "Item 1", new BigDecimal("10.0"), "Description 1");

        // Mock repository method
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Make the API call
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Item responseItem = responseEntity.getBody();
        assertNotNull(responseItem);
        assertEquals("Item 1", responseItem.getName());
        assertEquals(new BigDecimal("10.0"), responseItem.getPrice());
        assertEquals("Description 1", responseItem.getDescription());
    }

    @Test
    public void testGetItemsByName() {
        // Create a list of test items
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Item 1", new BigDecimal("10.0"), "Description 1"));
        items.add(new Item(2L, "Item 1", new BigDecimal("20.0"), "Description 2"));

        // Mock repository method
        when(itemRepository.findByName("Item 1")).thenReturn(items);

        // Make the API call
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Item 1");

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Item> responseItems = responseEntity.getBody();
        assertNotNull(responseItems);
        assertEquals(2, responseItems.size());
    }
}
