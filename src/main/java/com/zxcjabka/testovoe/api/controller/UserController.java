package com.zxcjabka.testovoe.api.controller;

import com.zxcjabka.testovoe.api.service.UserService;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationResponse;
import com.zxcjabka.testovoe.api.service.model.UserRegistrationModel;
import com.zxcjabka.testovoe.api.service.model.UserRegistrationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<UserRegistrationResponse> createUser(@RequestBody @Valid UserRegistrationModel model) {
        return ResponseEntity.ok(userService.createUser(model));
    }


}
