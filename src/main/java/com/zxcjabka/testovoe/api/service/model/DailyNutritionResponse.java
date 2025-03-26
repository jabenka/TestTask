package com.zxcjabka.testovoe.api.service.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class DailyNutritionResponse {
    LocalDate date;
    double totalCalories;
    int mealsCount;
    boolean goalAchieved;
}