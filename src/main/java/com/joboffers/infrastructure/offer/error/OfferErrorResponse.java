package com.joboffers.infrastructure.offer.error;

import org.springframework.http.HttpStatus;

public record OfferErrorResponse(
        String message,
        HttpStatus status
) {
}
