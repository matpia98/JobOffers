package com.joboffers.domain.loginandregister.dto;

import lombok.Builder;

@Builder
public record CreateUserDto(
        String username,
        String password
) {
}
