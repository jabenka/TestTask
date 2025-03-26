package com.zxcjabka.testovoe.api.service.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishRegistrationResponse {
    @JsonProperty("name")
    String name;
    @JsonProperty("numberOfCalories")
    Double numberOfCalories;
}
