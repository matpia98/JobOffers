package com.joboffers.infrastructure.offer.error;

import com.joboffers.domain.offer.OfferNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Log4j2
public class OfferControllerErrorHandler {
    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<OfferErrorResponse> handleOfferNotFoundException(OfferNotFoundException exception){
        String message = exception.getMessage();
        log.error(message);
        return ResponseEntity.status(NOT_FOUND).body(new OfferErrorResponse(message, NOT_FOUND));
    }

}
