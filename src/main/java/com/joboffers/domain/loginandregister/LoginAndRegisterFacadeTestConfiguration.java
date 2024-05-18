package com.joboffers.domain.loginandregister;

public class LoginAndRegisterFacadeTestConfiguration {

    static LoginAndRegisterFacade createForTest(UserRepository userRepository) {
        UserValidator userValidator = new UserValidator(userRepository);
        return new LoginAndRegisterFacade(userRepository, userValidator);
    }
}
