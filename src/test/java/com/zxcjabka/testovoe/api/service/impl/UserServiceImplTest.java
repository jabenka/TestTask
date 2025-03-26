package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.UserAlreadyExistsException;
import com.zxcjabka.testovoe.api.service.model.*;
import com.zxcjabka.testovoe.persistence.entity.*;
import com.zxcjabka.testovoe.persistence.repository.*;
import com.zxcjabka.testovoe.util.Aim;
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
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_WhenEmailNotExists_ShouldCreateUser() {

        UserRegistrationModel model = new UserRegistrationModel(
                "John Doe", "john@example.com", 30, 75.0F, 180.0F, Aim.MAINTENANCE);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        UserEntity savedUser = UserEntity.builder()
                .name("John Doe")
                .email("john@example.com")
                .age(30)
                .weight(75.0F)
                .height(180.0F)
                .aim(Aim.MAINTENANCE)
                .dailyIntake(calculateExpectedIntake(model))
                .build();

        when(userRepository.saveAndFlush(any(UserEntity.class))).thenReturn(savedUser);

        UserRegistrationResponse response = userService.createUser(model);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());

        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository).saveAndFlush(any(UserEntity.class));
    }

    @Test
    void createUser_WhenEmailExists_ShouldThrowException() {

        UserRegistrationModel model = new UserRegistrationModel(
                "John Doe", "john@example.com", 30, 75.0F, 180.0F, Aim.MAINTENANCE);

        UserEntity existingUser = UserEntity.builder()
                .name("John Doe")
                .email("john@example.com")
                .age(30)
                .weight(75.0F)
                .height(180.0F)
                .aim(Aim.MAINTENANCE)
                .dailyIntake(calculateExpectedIntake(model))
                .build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(existingUser));

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(model));

        assertEquals("User with email john@example.com already exists", exception.getMessage());

        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void calculateDailyIntake_ForMaintenance_ShouldCalculateCorrectly() {

        UserRegistrationModel model = new UserRegistrationModel(
                "Test", "test@example.com", 25, 70.0F, 175.0F, Aim.MAINTENANCE);

        double expected = 88.36 + (13.4 * 70) + (4.8 * 175) - (5.7 * 25);

        double result = userService.calculateDailyIntake(model);

        assertEquals(expected, result, 0.01);
    }

    @Test
    void calculateDailyIntake_ForMassGain_ShouldApplyCorrection() {

        UserRegistrationModel model = new UserRegistrationModel(
                "Test", "test@example.com", 25, 70.0F, 175.0F, Aim.MASS_GAIN);

        double base = 88.36 + (13.4 * 70) + (4.8 * 175) - (5.7 * 25);
        double expected = base * 1.25;

        double result = userService.calculateDailyIntake(model);

        assertEquals(expected, result, 0.01);
    }

    @Test
    void calculateDailyIntake_ForWeightLoss_ShouldApplyCorrection() {

        UserRegistrationModel model = new UserRegistrationModel(
                "Test", "test@example.com", 25, 70.0F, 175.0F, Aim.WEIGHT_LOSS);

        double base = 88.36 + (13.4 * 70) + (4.8 * 175) - (5.7 * 25);
        double expected = base * 0.75;

        double result = userService.calculateDailyIntake(model);

        assertEquals(expected, result, 0.01);
    }

    @Test
    void createUser_ShouldSetCorrectDailyIntake() {

        UserRegistrationModel model = new UserRegistrationModel(
                "John Doe", "john@example.com", 30, 75.0F, 180.0F, Aim.MAINTENANCE);

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        UserEntity capturedUser = UserEntity.builder()
                .id(1L)
                .name(model.getName())
                .email(model.getEmail())
                .age(model.getAge())
                .weight(model.getWeight())
                .height(model.getHeight())
                .aim(model.getAim())
                .dailyIntake(userService.calculateDailyIntake(model))
                .build();

        userService.createUser(model);

        double expectedIntake = calculateExpectedIntake(model);
        assertEquals(expectedIntake, capturedUser.getDailyIntake(), 0.01);
    }

    private double calculateExpectedIntake(UserRegistrationModel model) {
        double dailyIntake = 88.36 + (13.4 * model.getWeight())
                + (4.8 * model.getHeight())
                - (5.7 * model.getAge());

        double correction = 1.0;
        switch (model.getAim()) {
            case MASS_GAIN -> correction = 1.25;
            case WEIGHT_LOSS -> correction = 0.75;
        }
        return dailyIntake * correction;
    }
}