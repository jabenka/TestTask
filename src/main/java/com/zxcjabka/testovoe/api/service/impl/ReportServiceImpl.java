package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.NoMealsException;
import com.zxcjabka.testovoe.api.exception.UserNotFoundException;
import com.zxcjabka.testovoe.api.service.ReportService;
import com.zxcjabka.testovoe.api.service.model.CalorieCheckResponse;
import com.zxcjabka.testovoe.api.service.model.DailyNutritionResponse;
import com.zxcjabka.testovoe.api.service.model.DailyReportResponse;
import com.zxcjabka.testovoe.persistence.entity.MealEntity;
import com.zxcjabka.testovoe.persistence.entity.UserEntity;
import com.zxcjabka.testovoe.persistence.repository.MealRepository;
import com.zxcjabka.testovoe.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportServiceImpl implements ReportService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }


    @Override
    public DailyReportResponse generateDailyReport(Long userId, LocalDate date) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<MealEntity> meals = mealRepository.findDailyMeals(userId, startOfDay,endOfDay);
        if (meals.isEmpty()) {
            throw new NoMealsException("No mils have found");
        }

        return DailyReportResponse.builder()
                .date(date)
                .totalMeals(meals.size())
                .totalCalories(meals.stream().mapToDouble(this::calculateCalories).sum())
                .meals(meals.stream()
                        .map(m -> DailyReportResponse.MealShortInfo.builder()
                                .calories(this.calculateCalories(m))
                                .build())
                        .toList())
                .build();
    }

    @Override
    public CalorieCheckResponse checkCalorieIntake(Long userId, LocalDate date) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        List<MealEntity> meals = mealRepository.findDailyMeals(userId, startOfDay,endOfDay);
        if (meals.isEmpty()) {
            throw new NoMealsException("No meals have found");
        }
        Double totalCalories=meals.stream().mapToDouble(this::calculateCalories).sum();
        Double target=userEntity.getDailyIntake();
        boolean goal=target>totalCalories;
        return CalorieCheckResponse.builder()
                .date(date)
                .consumedCalories(totalCalories)
                .dailyNorm(target)
                .isWithinLimit(goal)
                .difference(target-totalCalories)
                .build();
    }

    @Override
    public List<DailyNutritionResponse> getNutritionHistory(Long userId, LocalDate startDate, LocalDate endDate) {
        if (startDate == null) startDate = LocalDate.now().minusDays(7);
        if (endDate == null) endDate = LocalDate.now();
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }

        List<MealEntity> meals = mealRepository.findByUserIdAndCreatedAtBetween(
                userId,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        Map<LocalDate, List<MealEntity>> mealsByDay = meals.stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getCreatedAt().toLocalDate()
                ));

        List<DailyNutritionResponse> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<MealEntity> dayMeals = mealsByDay.getOrDefault(date, Collections.emptyList());

            double dayCalories = dayMeals.stream()
                    .mapToDouble(this::calculateCalories)
                    .sum();
            Boolean goal=checkCalories(userEntity,dayMeals);

            result.add(new DailyNutritionResponse(
                    date,
                    dayCalories,
                    dayMeals.size(),
                    goal

            ));
        }

        return result;
    }

    Boolean checkCalories(UserEntity entity, List<MealEntity> meal) {
            Double target=entity.getDailyIntake();
            Double total=meal.stream().mapToDouble(this::calculateCalories).sum();
            return target>total;
    }




    Double calculateCalories(MealEntity entity){
        return entity.getDishes().stream()
                .mapToDouble(md -> md.getDish().getNumberOfCalories())
                .sum();
    }

}
