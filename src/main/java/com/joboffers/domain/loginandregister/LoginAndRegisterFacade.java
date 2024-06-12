package com.joboffers.domain.loginandregister;

import com.joboffers.domain.loginandregister.dto.RegisterUserDto;
import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import com.joboffers.domain.loginandregister.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private final UserRepository userRepository;
    private final UserValidator userValidator;


    public RegistrationResultDto registerUser(RegisterUserDto registerUserDto) {
        userValidator.validate(registerUserDto.username(), registerUserDto.password());
        User savedUser = userRepository.save(User.builder()
                .username(registerUserDto.username())
                .password(registerUserDto.password())
                .build());
        return new RegistrationResultDto(savedUser.getId(), savedUser.getUsername(), true);
    }

    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User with username: " + username + " not found"));
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
