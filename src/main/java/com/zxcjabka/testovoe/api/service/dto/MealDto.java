package com.zxcjabka.testovoe.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealDto {
    @JsonProperty("id")
    Long id;

    @JsonProperty("userId")
    @NonNull
    Long userId;

    @JsonProperty("createdAt")
    @NonNull
    LocalDateTime createdAt;

    @JsonProperty("dishes")
    @NonNull
    List<MealDishDto> dishes;
}