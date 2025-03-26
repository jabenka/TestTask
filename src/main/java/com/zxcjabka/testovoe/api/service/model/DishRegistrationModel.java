package com.zxcjabka.testovoe.api.service.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class DishRegistrationModel {

    @NotBlank(message = "Название блюда не может быть пустым")
    String name;

    @NotNull(message = "Калории не могут быть пустыми")
    @DecimalMin(value = "0.1", message = "Калории должны быть положительными")
    Double numberOfCalories;

    @NotNull(message = "Белки не могут быть пустыми")
    @DecimalMin(value = "0.0", message = "Белки не могут быть отрицательными")
    Double proteins;

    @NotNull(message = "Жиры не могут быть пустыми")
    @DecimalMin(value = "0.0", message = "Жиры не могут быть отрицательными")
    Double fats;

    @NotNull(message = "Углеводы не могут быть пустыми")
    @DecimalMin(value = "0.0", message = "Углеводы не могут быть отрицательными")
    Double carbohydrates;
}