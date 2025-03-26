package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.DishAlreadyExistsException;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationModel;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationResponse;
import com.zxcjabka.testovoe.persistence.entity.DishEntity;
import com.zxcjabka.testovoe.persistence.repository.DishRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishServiceImpl dishService;

    @Test
    void createDish_WhenDishDoesNotExist_ShouldCreateAndReturnResponse() {

        DishRegistrationModel model = new DishRegistrationModel(
                "Test Dish", 500.0, 30.0, 20.0, 50.0);

        DishEntity savedEntity = DishEntity.builder()
                .name("Test Dish")
                .numberOfCalories(500.0)
                .proteins(30.0)
                .fats(20.0)
                .carbohydrates(50.0)
                .build();

        when(dishRepository.findByName("Test Dish")).thenReturn(Optional.empty());
        when(dishRepository.saveAndFlush(any(DishEntity.class))).thenReturn(savedEntity);

        DishRegistrationResponse response = dishService.createDish(model);

        assertNotNull(response);
        assertEquals("Test Dish", response.getName());
        assertEquals(500, response.getNumberOfCalories());

        verify(dishRepository).findByName("Test Dish");
        verify(dishRepository).saveAndFlush(any(DishEntity.class));
    }

    @Test
    void createDish_WhenDishAlreadyExists_ShouldThrowException() {

        DishRegistrationModel model = new DishRegistrationModel(
                "Existing Dish", 400.0, 25.0, 15.0, 40.0);

        DishEntity existingEntity = DishEntity.builder()
                .name("Existing Dish")
                .numberOfCalories(500.0)
                .proteins(30.0)
                .fats(20.0)
                .carbohydrates(50.0)
                .build();

        when(dishRepository.findByName("Existing Dish")).thenReturn(Optional.of(existingEntity));

        DishAlreadyExistsException exception = assertThrows(DishAlreadyExistsException.class,
                () -> dishService.createDish(model));

        assertEquals("Dish with name Existing Dish already exists", exception.getMessage());

        verify(dishRepository).findByName("Existing Dish");
        verify(dishRepository, never()).saveAndFlush(any());
    }


    @Test
    void createDish_ResponseShouldContainCorrectData() {

        DishRegistrationModel model = new DishRegistrationModel(
                "Response Test", 600.0, 40.0, 30.0, 20.0);

        DishEntity savedEntity = DishEntity.builder()
                .name("Response Test")
                .numberOfCalories(600.0)
                .proteins(40.0)
                .fats(30.0)
                .carbohydrates(20.0)
                .build();

        when(dishRepository.findByName("Response Test")).thenReturn(Optional.empty());
        when(dishRepository.saveAndFlush(any(DishEntity.class))).thenReturn(savedEntity);

        DishRegistrationResponse response = dishService.createDish(model);

        assertEquals(savedEntity.getName(), response.getName());
        assertEquals(savedEntity.getNumberOfCalories(), response.getNumberOfCalories());
    }
}