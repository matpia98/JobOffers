package com.joboffers.infrastructure.loginandregister.controller;

import lombok.Builder;

@Builder
public record JwtResponseDto(
        String username,
        String token
) {
}
