package com.zxcjabka.testovoe.persistence.repository;

import com.zxcjabka.testovoe.persistence.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(@NonNull String email);
}
