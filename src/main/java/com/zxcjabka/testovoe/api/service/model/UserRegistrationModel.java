package com.zxcjabka.testovoe.api.service.model;

import com.zxcjabka.testovoe.util.Aim;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRegistrationModel {

    @NotBlank(message = "Имя не может быть пустым")
    String name;

    @Email(message = "Некоретный email")
    @NotBlank(message = "Email не может быть пустым")
    String email;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 0,message = "Возраст не может быть отрицательным")
    @Max(value = 150,message = "Вы побили рекорд по долгожительству")
    Integer age;

    @NotNull(message = "Вес не может быть пустым")
    @Min(value = 15,message="Вес не может быть меньше допустимого для жизни")
    @Max(value = 700,message = "Вес не может быть больше допустимого для жизни")
    Float weight;

    @NotNull(message = "Рост не может быть путсым")
    @Min(value = 0,message = "Рост не может быть отрицательным")
    @Max(value = 4,message = "Рост не может быть больше допустимого для жизни")
    Float height;

    @NotNull(message = "Цель не может быть пустой")
    Aim aim;

}
