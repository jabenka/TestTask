package com.zxcjabka.testovoe.api.service.impl;

import com.zxcjabka.testovoe.api.exception.UserAlreadyExistsException;
import com.zxcjabka.testovoe.api.service.UserService;
import com.zxcjabka.testovoe.api.service.model.UserRegistrationModel;
import com.zxcjabka.testovoe.api.service.model.UserRegistrationResponse;
import com.zxcjabka.testovoe.persistence.entity.UserEntity;
import com.zxcjabka.testovoe.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserRegistrationResponse createUser(UserRegistrationModel userRegistrationModel) {

        if (userRepository.findByEmail(userRegistrationModel.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + userRegistrationModel.getEmail() + " already exists");
        }
        Double dailyIntake = calculateDailyIntake(userRegistrationModel);

        UserEntity userEntity = UserEntity.builder()
                .name(userRegistrationModel.getName())
                .email(userRegistrationModel.getEmail())
                .age(userRegistrationModel.getAge())
                .weight(userRegistrationModel.getWeight())
                .height(userRegistrationModel.getHeight())
                .aim(userRegistrationModel.getAim())
                .dailyIntake(dailyIntake)
                .build();

        userRepository.saveAndFlush(userEntity);

        return new UserRegistrationResponse(userEntity.getName(), userEntity.getEmail());
    }

    /**
     * @param userRegistrationModel-representing model of the user
     * @apiNote correction used for correction dailyIntake,
     * according to the purpose(mass gain,weight loss)
     * @return the daily calories intake
     */
    Double calculateDailyIntake(UserRegistrationModel userRegistrationModel){
        double dailyIntake=88.36 + (13.4 * userRegistrationModel.getWeight())
                + (4.8 * userRegistrationModel.getHeight())
                - (5.7 * userRegistrationModel.getAge());
        double correction=1.0;
        switch (userRegistrationModel.getAim()){
            case MASS_GAIN -> correction=1.25;
            case WEIGHT_LOSS -> correction=0.75;
        }
        dailyIntake*=correction;
        return dailyIntake;
    }
}
