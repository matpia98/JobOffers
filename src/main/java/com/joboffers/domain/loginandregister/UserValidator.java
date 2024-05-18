package com.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class UserValidator {

    private final UserRepository userRepository;
    boolean validate(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameNotUniqueException("Username: " + username + " already exists");
        }
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidRegistrationDataException("Invalid username or password data");
        }
        return true;
    }
}
