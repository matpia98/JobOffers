package com.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class LoginAndRegisterFacadeTestConfiguration {

    private final UserRepository userRepository;

    static LoginAndRegisterFacade createForTest(UserRepository userRepository) {
        UserValidator userValidator = new UserValidator(userRepository);
        return new LoginAndRegisterFacade(userRepository, userValidator);
    }

    @Bean
    public UserValidator userValidator() {
        return new UserValidator(userRepository);
    }

}
