package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.OfferRequestDto;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
class OfferValidator {

    private final OfferRepository offerRepository;

    boolean validate(OfferRequestDto dto) {
        if (dto.jobTitle() == null || dto.jobTitle().isEmpty()) {
            throw new OfferNotValidException("Job title cannot be null or empty");
        }
        if (dto.companyName() == null || dto.companyName().isEmpty()) {
            throw new OfferNotValidException("Company name cannot be null or empty");
        }
        if (dto.salary() == null) {
            throw new OfferNotValidException("Salary cannot be null");
        }
        if (dto.url() == null || dto.url().isEmpty()) {
            throw new OfferNotValidException("URL cannot be null or empty");
        }
        return true;
    }

    boolean validateUrl(String url) {
        Optional<Offer> offerByUrl = offerRepository.findByUrl(url);
        if (offerByUrl.isPresent()) {
            throw new DuplicateUrlException("Offer with URL " + url + " already exists");
        }
        return true;
    }
}
