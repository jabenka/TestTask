package com.zxcjabka.testovoe.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "dishes")
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @NonNull
    String name;
    @NonNull
    Double numberOfCalories;
    @NonNull
    Double proteins;
    @NonNull
    Double fats;
    @NonNull
    Double carbohydrates;

}
