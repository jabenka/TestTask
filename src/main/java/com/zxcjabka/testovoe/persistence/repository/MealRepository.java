package com.zxcjabka.testovoe.persistence.repository;

import com.zxcjabka.testovoe.persistence.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<MealEntity,Long> {
    List<MealEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT m FROM MealEntity m WHERE m.user.id = :userId " +
            "AND m.createdAt >= :start AND m.createdAt < :end")
    List<MealEntity> findDailyMeals(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<MealEntity> findByUserIdAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
