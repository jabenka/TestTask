package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.NoMealsException;
import com.zxcjabka.testovoe.api.exception.UserNotFoundException;
import com.zxcjabka.testovoe.api.service.model.DailyNutritionResponse;
import com.zxcjabka.testovoe.persistence.entity.*;
import com.zxcjabka.testovoe.persistence.repository.MealRepository;
import com.zxcjabka.testovoe.persistence.repository.UserRepository;
import com.zxcjabka.testovoe.util.Aim;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private MealEntity createMeal(double calories, LocalDateTime date) {

        DishEntity dish = DishEntity.builder()
                .id(1L)
                .name("test")
                .numberOfCalories(calories)
                .proteins(calories / 2)
                .fats(calories / 2)
                .carbohydrates(calories / 2)
                .build();

        MealDishEntity mealDish = MealDishEntity.builder()
                .dish(dish)
                .build();

        return MealEntity.builder()
                .dishes(List.of(mealDish))
                .createdAt(date)
                .build();
    }

    private UserEntity createUser(double dailyIntake) {
        return UserEntity.builder()
                .dailyIntake(dailyIntake)
                .build();
    }

    @Test
    void generateDailyReport_ShouldReturnCorrectReport_WhenMealsExist() {

        Long userId = 1L;
        LocalDate date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        List<MealEntity> meals = List.of(
                createMeal(500.0, start.plusHours(1)),
                createMeal(300.0, start.plusHours(12))
        );

        when(mealRepository.findDailyMeals(userId, start, end)).thenReturn(meals);

        var result = reportService.generateDailyReport(userId, date);

        assertNotNull(result);
        assertEquals(date, result.getDate());
        assertEquals(2, result.getTotalMeals());
        assertEquals(800.0, result.getTotalCalories());
        assertEquals(2, result.getMeals().size());
        verify(mealRepository).findDailyMeals(userId, start, end);
    }

    @Test
    void generateDailyReport_ShouldThrowNoMealsException_WhenNoMealsFound() {

        Long userId = 1L;
        LocalDate date = LocalDate.now();

        when(mealRepository.findDailyMeals(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(NoMealsException.class,
                () -> reportService.generateDailyReport(userId, date));
    }

    @Test
    void checkCalorieIntake_ShouldReturnCorrectResponse_WhenDataExists() {

        Long userId = 1L;
        LocalDate date = LocalDate.now();
        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(2000.0).build();
        List<MealEntity> meals = List.of(
                createMeal(800.0, date.atStartOfDay()),
                createMeal(500.0, date.atStartOfDay().plusHours(6))
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findDailyMeals(anyLong(), any(), any())).thenReturn(meals);

        var result = reportService.checkCalorieIntake(userId, date);

        assertNotNull(result);
        assertEquals(date, result.getDate());
        assertEquals(1300.0, result.getConsumedCalories());
        assertEquals(2000.0, result.getDailyNorm());
        assertTrue(result.isWithinLimit());
        assertEquals(700.0, result.getDifference());
    }

    @Test
    void checkCalorieIntake_ShouldThrowUserNotFoundException_WhenUserNotFound() {

        Long userId = 1L;
        LocalDate date = LocalDate.now();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> reportService.checkCalorieIntake(userId, date));
    }

    @Test
    void checkCalorieIntake_ShouldThrowNoMealsException_WhenNoMealsFound() {

        Long userId = 1L;
        LocalDate date = LocalDate.now();
        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(2000.0).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findDailyMeals(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(NoMealsException.class,
                () -> reportService.checkCalorieIntake(userId, date));
    }

    @Test
    void getNutritionHistory_ShouldReturnCorrectResponse_WhenDataExists() {

        Long userId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();
        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(2000.0).build();

        List<MealEntity> meals = List.of(
                createMeal(500.0, startDate.atStartOfDay()),
                createMeal(300.0, startDate.atStartOfDay().plusHours(6)),
                createMeal(800.0, endDate.atStartOfDay())
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndCreatedAtBetween(anyLong(), any(), any()))
                .thenReturn(meals);

        var result = reportService.getNutritionHistory(userId, startDate, endDate);

        assertNotNull(result);
        assertEquals(3, result.size());

        DailyNutritionResponse day1 = result.get(0);
        assertEquals(startDate, day1.getDate());
        assertEquals(800.0, day1.getTotalCalories());
        assertEquals(2, day1.getMealsCount());
        assertTrue(day1.isGoalAchieved());

        var day3 = result.get(2);
        assertEquals(endDate, day3.getDate());
        assertEquals(800.0, day3.getTotalCalories());
        assertEquals(1, day3.getMealsCount());
        assertTrue(day3.isGoalAchieved());
    }

    @Test
    void getNutritionHistory_ShouldUseDefaultDates_WhenNullDatesProvided() {
        Long userId = 1L;
        UserEntity user = UserEntity.builder().id(userId).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(2000.0).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mealRepository.findByUserIdAndCreatedAtBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        var result = reportService.getNutritionHistory(userId, null, null);

        assertNotNull(result);
        verify(mealRepository).findByUserIdAndCreatedAtBetween(
                eq(userId),
                any(LocalDateTime.class),
                any(LocalDateTime.class));
    }

    @Test
    void getNutritionHistory_ShouldThrowIllegalArgumentException_WhenStartAfterEnd() {

        Long userId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);

        assertThrows(IllegalArgumentException.class,
                () -> reportService.getNutritionHistory(userId, startDate, endDate));
    }

    @Test
    void getNutritionHistory_ShouldThrowUserNotFoundException_WhenUserNotFound() {

        Long userId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> reportService.getNutritionHistory(userId, startDate, endDate));
    }

    @Test
    void checkCalories_ShouldReturnCorrectBoolean() {

        UserEntity user = UserEntity.builder().id(1L).name("").email("")
                .age(0).weight(10.0F).height(2.0F).aim(Aim.MAINTENANCE).dailyIntake(2000.0).build();
        List<MealEntity> mealsUnder = List.of(createMeal(1000.0, LocalDateTime.now()));
        List<MealEntity> mealsOver = List.of(createMeal(2000.0, LocalDateTime.now()));

        assertTrue(reportService.checkCalories(user, mealsUnder));
        assertFalse(reportService.checkCalories(user, mealsOver));
    }

    @Test
    void calculateCalories_ShouldReturnCorrectSum() {

        DishEntity dish1 = DishEntity.builder().id(1L)
                .name("test")
                .numberOfCalories(300.0)
                .proteins(300.0/ 2)
                .fats(300.0 / 2)
                .carbohydrates(300.0 / 2)
                .build();
        DishEntity dish2 = DishEntity.builder().id(1L)
                .name("test")
                .numberOfCalories(500.0)
                .proteins(500.0/ 2)
                .fats(500.0 / 2)
                .carbohydrates(500.0 / 2)
                .build();

        MealDishEntity md1 = MealDishEntity.builder().dish(dish1).build();
        MealDishEntity md2 = MealDishEntity.builder().dish(dish2).build();

        MealEntity meal = MealEntity.builder()
                .dishes(List.of(md1, md2))
                .build();

        assertEquals(800.0, reportService.calculateCalories(meal));
    }
}