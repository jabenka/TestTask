package com.zxcjabka.testovoe.api.service.model;

import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealDishRequest {
    @NonNull
    Long dishId;
}
