package com.zxcjabka.testovoe.api.service.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyReportResponse {
    LocalDate date;
    Integer totalMeals;
    Double totalCalories;
    List<MealShortInfo> meals;

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MealShortInfo {
        Double calories;
    }
}