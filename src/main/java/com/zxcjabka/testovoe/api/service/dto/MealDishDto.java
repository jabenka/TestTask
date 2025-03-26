package com.zxcjabka.testovoe.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealDishDto {
    @JsonProperty("id")
    Long id;

    @JsonProperty("mealId")
    @NonNull
    Long mealId;

    @JsonProperty("dishId")
    @NonNull
    Long dishId;
}