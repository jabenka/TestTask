package com.zxcjabka.testovoe.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishDto {
    @JsonProperty("id")
    @NonNull
    Long id;
    @JsonProperty("name")
    @NonNull
    String name;
    @JsonProperty("numberOfCalories")
    @NonNull
    Double numberOfCalories;
    @JsonProperty("proteins")
    @NonNull
    Double proteins;
    @JsonProperty("fats")
    @NonNull
    Double fats;
    @JsonProperty("carbohydrates")
    @NonNull
    Double carbohydrates;
}
