package com.zxcjabka.testovoe.api.service;

import com.zxcjabka.testovoe.api.service.model.UserRegistrationModel;
import com.zxcjabka.testovoe.api.service.model.UserRegistrationResponse;
import org.springframework.stereotype.Service;


public interface UserService {

    public UserRegistrationResponse createUser(UserRegistrationModel userRegistrationModel);

}
