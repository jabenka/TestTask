package com.zxcjabka.testovoe.api.controller;

import com.zxcjabka.testovoe.api.service.DishService;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationModel;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dish")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping("/add")
    public ResponseEntity<?>addDish(@RequestBody @Valid DishRegistrationModel model) {
        return ResponseEntity.ok(dishService.createDish(model));
    }
}
