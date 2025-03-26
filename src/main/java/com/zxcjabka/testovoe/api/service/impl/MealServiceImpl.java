package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.DishNotFoundException;
import com.zxcjabka.testovoe.api.exception.UserNotFoundException;
import com.zxcjabka.testovoe.api.service.MealService;
import com.zxcjabka.testovoe.api.service.model.CreateMealRequest;
import com.zxcjabka.testovoe.api.service.model.MealDishRequest;
import com.zxcjabka.testovoe.api.service.model.MealDishResponse;
import com.zxcjabka.testovoe.api.service.model.MealResponse;
import com.zxcjabka.testovoe.persistence.entity.DishEntity;
import com.zxcjabka.testovoe.persistence.entity.MealDishEntity;
import com.zxcjabka.testovoe.persistence.entity.MealEntity;
import com.zxcjabka.testovoe.persistence.entity.UserEntity;
import com.zxcjabka.testovoe.persistence.repository.DishRepository;
import com.zxcjabka.testovoe.persistence.repository.MealRepository;
import com.zxcjabka.testovoe.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;

@Component
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public MealServiceImpl(MealRepository mealRepository, UserRepository userRepository, DishRepository dishRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    @Override
    public MealResponse createMeal(CreateMealRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        MealEntity meal = MealEntity.builder()
                .user(user)
                .createdAt(LocalDateTime.now())
                .dishes(new ArrayList<>())
                .build();

        for (MealDishRequest dishRequest : request.getDishes()) {
            DishEntity dish = dishRepository.findById(dishRequest.getDishId())
                    .orElseThrow(() -> new DishNotFoundException("Dish not found"));

            MealDishEntity mealDish = MealDishEntity.builder()
                    .meal(meal)
                    .dish(dish)
                    .build();

            meal.getDishes().add(mealDish);
        }

        mealRepository.saveAndFlush(meal);

        return mapToResponse(meal);
    }
    MealResponse mapToResponse(MealEntity meal) {
        double totalCalories = 0.0;
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbohydrates = 0.0;

        List<MealDishResponse> dishResponses = new ArrayList<>();

        for (MealDishEntity mealDish : meal.getDishes()) {
            DishEntity dish = mealDish.getDish();

            double dishCalories = dish.getNumberOfCalories();
            double dishProteins = dish.getProteins();
            double dishFats = dish.getFats();
            double dishCarbs = dish.getCarbohydrates();

            totalCalories += dishCalories;
            totalProteins += dishProteins;
            totalFats += dishFats;
            totalCarbohydrates += dishCarbs;

            MealDishResponse dishResponse = MealDishResponse.builder()
                    .dishId(dish.getId())
                    .dishName(dish.getName())
                    .calories(dishCalories)
                    .proteins(dishProteins)
                    .fats(dishFats)
                    .carbohydrates(dishCarbs)
                    .build();

            dishResponses.add(dishResponse);
        }

        return MealResponse.builder()
                .id(meal.getId())
                .userId(meal.getUser().getId())
                .createdAt(meal.getCreatedAt())
                .dishes(dishResponses)
                .totalCalories(totalCalories)
                .totalProteins(totalProteins)
                .totalFats(totalFats)
                .totalCarbohydrates(totalCarbohydrates)
                .build();
    }

    @Override
    public List<MealResponse> getUserMeals(Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mealRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .toList();

    }
}
