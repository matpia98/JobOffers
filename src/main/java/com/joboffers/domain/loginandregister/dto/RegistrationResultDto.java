package com.joboffers.domain.loginandregister.dto;

public record RegistrationResultDto(
        String id,
        String username,
        boolean created
) {
}
