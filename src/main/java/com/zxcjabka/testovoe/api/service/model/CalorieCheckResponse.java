package com.zxcjabka.testovoe.api.service.model;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CalorieCheckResponse {
    LocalDate date;
    double consumedCalories;
    double dailyNorm;
    boolean isWithinLimit;
    double difference;
}