package com.zxcjabka.testovoe.persistence.entity;


import com.zxcjabka.testovoe.util.Aim;
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
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull
    String name;
    @NonNull
    @Column(unique = true)
    String email;
    @NonNull
    Integer age;
    @NonNull
    Float weight;
    @NonNull
    Float height;
    @Enumerated(EnumType.STRING)
    @NonNull
    Aim aim;
    @NonNull
    Double dailyIntake;

}
