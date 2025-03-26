package com.zxcjabka.testovoe.api.controller;

import com.zxcjabka.testovoe.api.service.MealService;
import com.zxcjabka.testovoe.api.service.model.CreateMealRequest;
import com.zxcjabka.testovoe.api.service.model.MealResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<MealResponse> createMeal(@RequestBody CreateMealRequest request) {
        MealResponse response = mealService.createMeal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public List<MealResponse> getUserMeals(@PathVariable Long userId) {
        return mealService.getUserMeals(userId);
    }
}