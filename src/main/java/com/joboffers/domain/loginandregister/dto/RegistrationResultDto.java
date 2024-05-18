package com.joboffers.domain.loginandregister.dto;

public record RegistrationResultDto(
        Long id,
        String username,
        boolean created
) {
}
