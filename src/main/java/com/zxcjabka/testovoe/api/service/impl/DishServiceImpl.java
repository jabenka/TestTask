package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.DishAlreadyExistsException;
import com.zxcjabka.testovoe.api.service.DishService;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationModel;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationResponse;
import com.zxcjabka.testovoe.persistence.entity.DishEntity;
import com.zxcjabka.testovoe.persistence.repository.DishRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Transactional
    @Override
    public DishRegistrationResponse createDish(DishRegistrationModel model) {

        if(dishRepository.findByName(model.getName()).isPresent()) {
            throw new DishAlreadyExistsException("Dish with name "+ model.getName()+" already exists");
        }

        DishEntity dishEntity =DishEntity.builder()
                .name(model.getName())
                .numberOfCalories(model.getNumberOfCalories())
                .proteins(model.getProteins())
                .fats(model.getFats())
                .carbohydrates(model.getCarbohydrates())
                .build();
         dishRepository.saveAndFlush(dishEntity);

        return new DishRegistrationResponse(dishEntity.getName(),dishEntity.getNumberOfCalories());
    }
}
