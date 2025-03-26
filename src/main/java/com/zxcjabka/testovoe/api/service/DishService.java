package com.zxcjabka.testovoe.api.service;

import com.zxcjabka.testovoe.api.service.model.DishRegistrationModel;
import com.zxcjabka.testovoe.api.service.model.DishRegistrationResponse;

public interface DishService {

    public DishRegistrationResponse createDish(DishRegistrationModel model);

}
