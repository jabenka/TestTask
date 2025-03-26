package com.zxcjabka.testovoe.persistence.repository;

import com.zxcjabka.testovoe.persistence.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<DishEntity,Long> {
    Optional<DishEntity> findByName(String name);
}
