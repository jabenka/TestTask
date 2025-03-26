package com.zxcjabka.testovoe.api.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MealResponse {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private List<MealDishResponse> dishes;
    private Double totalCalories;
    private Double totalProteins;
    private Double totalFats;
    private Double totalCarbohydrates;
}
