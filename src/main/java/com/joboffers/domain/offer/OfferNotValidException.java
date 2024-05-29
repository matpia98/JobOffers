package com.joboffers.domain.offer;

public class OfferNotValidException extends RuntimeException {
    public OfferNotValidException(String message) {
        super(message);
    }
}
