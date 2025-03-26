package com.zxcjabka.testovoe.api.service.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealDishResponse {
    private Long dishId;
    private String dishName;
    private Double calories;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;
}
