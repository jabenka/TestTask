package com.zxcjabka.testovoe.api.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zxcjabka.testovoe.util.Aim;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @JsonProperty("id")
    @NonNull
    Long id;
    @JsonProperty("name")
    @NonNull
    String name;
    @JsonProperty("email")
    @NonNull
    String email;
    @JsonProperty("age")
    @NonNull
    Integer age;
    @JsonProperty("weight")
    @NonNull
    Float weight;
    @JsonProperty("height")
    @NonNull
    Float height;
    @JsonProperty("aim")
    @NonNull
    Aim aim;
    @JsonProperty("dailyIntake")
    @NonNull
    Double dailyIntake;
}
