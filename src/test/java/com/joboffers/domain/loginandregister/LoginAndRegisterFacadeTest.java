package com.joboffers.domain.loginandregister;

import com.joboffers.domain.loginandregister.dto.CreateUserDto;
import com.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import com.joboffers.domain.loginandregister.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class LoginAndRegisterFacadeTest {

    private final UserRepository userRepository= new InMemoryUserRepository();

    @Test
    public void should_register_user() {
        // given
        LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterFacadeTestConfiguration.createForTest(userRepository);
        CreateUserDto userToSave = CreateUserDto.builder()
                .username("username123")
                .password("username")
                .build();
        // when
        RegistrationResultDto savedUserDto = loginAndRegisterFacade.registerUser(userToSave);

        // then
        assertThat(savedUserDto.id()).isEqualTo(0L);
        assertThat(savedUserDto.username()).isEqualTo("username123");
        Assertions.assertTrue(savedUserDto.created());
    }

    @Test
    public void should_find_user_by_username() {
        // given
        LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterFacadeTestConfiguration.createForTest(userRepository);
        CreateUserDto userToSave = CreateUserDto.builder()
                .username("username123")
                .password("username")
                .build();
        RegistrationResultDto savedUserDto = loginAndRegisterFacade.registerUser(userToSave);

        // when
        UserDto userByUsername = loginAndRegisterFacade.findUserByUsername(savedUserDto.username());

        // then
        assertThat(userByUsername.id()).isEqualTo(savedUserDto.id());
        assertThat(userByUsername.username()).isEqualTo(savedUserDto.username());
    }

    @Test
    public void should_throw_exception_when_user_not_found() {
        // given
        LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterFacadeTestConfiguration.createForTest(userRepository);
        CreateUserDto userToSave = CreateUserDto.builder()
                .username("username123")
                .password("username")
                .build();
        loginAndRegisterFacade.registerUser(userToSave);

        // when
        Throwable throwable = catchThrowable(() -> loginAndRegisterFacade.findUserByUsername("username321"));

        // then
        assertThat(throwable).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User with username: username321 not found");
    }

    @Test
    public void should_throw_exception_when_user_gave_invalid_data_in_registering() {
        // given
        LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterFacadeTestConfiguration.createForTest(userRepository);
        CreateUserDto userToSave = CreateUserDto.builder()
                .username("username123")
                .password("")
                .build();
        // when
        Throwable throwable = catchThrowable(() -> loginAndRegisterFacade.registerUser(userToSave));

        // then
        assertThat(throwable).isInstanceOf(InvalidRegistrationDataException.class)
                .hasMessage("Invalid username or password data");
    }

    @Test
    public void should_throw_exception_when_user_gave_username_in_registering_which_exists() {
        // given
        LoginAndRegisterFacade loginAndRegisterFacade = LoginAndRegisterFacadeTestConfiguration.createForTest(userRepository);
        CreateUserDto userToSave = CreateUserDto.builder()
                .username("username123")
                .password("username")
                .build();
        loginAndRegisterFacade.registerUser(userToSave);

        CreateUserDto userToSave2 = CreateUserDto.builder()
                .username("username123")
                .password("username123")
                .build();
        // when
        Throwable throwable = catchThrowable(() -> loginAndRegisterFacade.registerUser(userToSave2));

        // then
        assertThat(throwable).isInstanceOf(UsernameNotUniqueException.class)
                .hasMessage("Username: username123 already exists");
    }

}