package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.DishNotFoundException;
import com.zxcjabka.testovoe.api.exception.UserNotFoundException;
import com.zxcjabka.testovoe.api.service.model.*;
import com.zxcjabka.testovoe.persistence.entity.*;
import com.zxcjabka.testovoe.persistence.repository.*;
import com.zxcjabka.testovoe.util.Aim;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private MealServiceImpl mealService;

    @Test
    void createMeal_WhenUserAndDishesExist_ShouldCreateMeal() {

        Long userId = 1L;
        Long dishId1 = 1L;
        Long dishId2 = 2L;

        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(0.0).build();
        DishEntity dish1 = DishEntity.builder()
                .id(dishId1)
                .name("Dish 1")
                .numberOfCalories(300.0)
                .proteins(20.0)
                .fats(10.0)
                .carbohydrates(30.0)
                .build();
        DishEntity dish2 = DishEntity.builder()
                .id(dishId2)
                .name("Dish 2")
                .numberOfCalories(400.0)
                .proteins(25.0)
                .fats(15.0)
                .carbohydrates(40.0)
                .build();

        CreateMealRequest request = new CreateMealRequest();
        request.setUserId(userId);
        request.setDishes(List.of(
                new MealDishRequest(dishId1),
                new MealDishRequest(dishId2)
        ));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dishRepository.findById(dishId1)).thenReturn(Optional.of(dish1));
        when(dishRepository.findById(dishId2)).thenReturn(Optional.of(dish2));

        MealEntity savedMeal = MealEntity.builder()
                .id(1L)
                .user(user)
                .createdAt(LocalDateTime.now())
                .dishes(new ArrayList<>())
                .build();
        when(mealRepository.saveAndFlush(any(MealEntity.class))).thenReturn(savedMeal);

        MealResponse response = mealService.createMeal(request);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(2, response.getDishes().size());
        assertEquals(700.0, response.getTotalCalories());
        assertEquals(45.0, response.getTotalProteins());
        assertEquals(25.0, response.getTotalFats());
        assertEquals(70.0, response.getTotalCarbohydrates());

        verify(userRepository).findById(userId);
        verify(dishRepository, times(2)).findById(any());
        verify(mealRepository).saveAndFlush(any(MealEntity.class));
    }

    @Test
    void createMeal_WhenUserNotFound_ShouldThrowException() {

        Long userId = 1L;
        CreateMealRequest request = new CreateMealRequest();
        request.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> mealService.createMeal(request));
        verify(userRepository).findById(userId);
        verifyNoInteractions(dishRepository);
        verifyNoInteractions(mealRepository);
    }

    @Test
    void createMeal_WhenDishNotFound_ShouldThrowException() {

        Long userId = 1L;
        Long dishId = 1L;

        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(0.0).build();
        CreateMealRequest request = new CreateMealRequest();
        request.setUserId(userId);
        request.setDishes(List.of(new MealDishRequest(dishId)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class, () -> mealService.createMeal(request));
        verify(userRepository).findById(userId);
        verify(dishRepository).findById(dishId);
        verifyNoInteractions(mealRepository);
    }

    @Test
    void getUserMeals_WhenUserExists_ShouldReturnMeals() {

        Long userId = 1L;
        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(0.0).build();

        MealEntity meal1 = MealEntity.builder()
                .id(1L)
                .user(user)
                .createdAt(LocalDateTime.now().minusDays(1))
                .dishes(new ArrayList<>())
                .build();
        MealEntity meal2 = MealEntity.builder()
                .id(2L)
                .user(user)
                .createdAt(LocalDateTime.now())
                .dishes(new ArrayList<>())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdOrderByCreatedAtDesc(userId))
                .thenReturn(List.of(meal2, meal1));

        List<MealResponse> result = mealService.getUserMeals(userId);

        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());

        verify(userRepository).findById(userId);
        verify(mealRepository).findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Test
    void getUserMeals_WhenUserNotFound_ShouldThrowException() {

        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> mealService.getUserMeals(userId));
        verify(userRepository).findById(userId);
        verifyNoInteractions(mealRepository);
    }

    @Test
    void mapToResponse_ShouldCalculateTotalsCorrectly() {

        Long userId = 1L;
        UserEntity user = UserEntity.builder().id(userId)
                .name("").email("").age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(0.0).build();
        DishEntity dish1 = DishEntity.builder()
                .id(1L)
                .name("Dish 1")
                .numberOfCalories(300.0)
                .proteins(20.0)
                .fats(10.0)
                .carbohydrates(30.0)
                .build();
        DishEntity dish2 = DishEntity.builder()
                .id(2L)
                .name("Dish 2")
                .numberOfCalories(400.0)
                .proteins(25.0)
                .fats(15.0)
                .carbohydrates(40.0)
                .build();

        MealEntity meal = MealEntity.builder()
                .id(1L)
                .user(user)
                .createdAt(LocalDateTime.now())
                .dishes(new ArrayList<>())
                .build();

        MealDishEntity mealDish1 = MealDishEntity.builder()
                .meal(meal)
                .dish(dish1)
                .build();
        MealDishEntity mealDish2 = MealDishEntity.builder()
                .meal(meal)
                .dish(dish2)
                .build();
        meal.getDishes().add(mealDish1);
        meal.getDishes().add(mealDish2);

        MealResponse response = mealService.mapToResponse(meal);

        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(2, response.getDishes().size());
        assertEquals(700.0, response.getTotalCalories());
        assertEquals(45.0, response.getTotalProteins());
        assertEquals(25.0, response.getTotalFats());
        assertEquals(70.0, response.getTotalCarbohydrates());
    }
}