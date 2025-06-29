package com.joboffers.infrastructure.security.jwt;

import com.joboffers.domain.loginandregister.LoginAndRegisterFacade;
import com.joboffers.domain.loginandregister.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@AllArgsConstructor
public class LoginUserDetailsService implements UserDetailsService {

    private final LoginAndRegisterFacade loginAndRegisterFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = loginAndRegisterFacade.findUserByUsername(username);
        return getUser(userDto);
    }

    private User getUser(UserDto user) {
        return new User(
                user.username(),
                user.password(),
                Collections.emptyList()
        );
    }
}
