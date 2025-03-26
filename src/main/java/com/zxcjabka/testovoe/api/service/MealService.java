package com.zxcjabka.testovoe.api.service;

import com.zxcjabka.testovoe.api.service.model.CreateMealRequest;
import com.zxcjabka.testovoe.api.service.model.MealResponse;

import java.util.List;

public interface MealService {

    public MealResponse createMeal(CreateMealRequest request);

    List<MealResponse> getUserMeals(Long userId);
}
