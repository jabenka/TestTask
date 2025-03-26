package com.zxcjabka.testovoe.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "meal_dishes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MealDishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "meal_id", nullable = false)
    MealEntity meal;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    DishEntity dish;

}
