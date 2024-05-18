package com.joboffers.domain.offer;

public class OfferNotValidException extends RuntimeException {
    OfferNotValidException(String message) {
        super(message);
    }
}
