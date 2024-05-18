package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.JobOfferResponse;
import com.joboffers.domain.offer.dto.OfferResponseDto;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
class OfferService {

    private final OfferFetcher offerFetcher;
    private final OfferRepository offerRepository;

    List<OfferResponseDto> fetchAndSaveAllOffersIfNotExists() {
        List<JobOfferResponse> fetchedOffers = offerFetcher.fetchOffers();
        List<JobOfferResponse> filteredOffers = filterOffersWithSameUrl(fetchedOffers);

        List<Offer> offersToSave = filteredOffers.stream()
                .map(OfferMapper::mapFromOfferResponseToOffer)
                .collect(Collectors.toList());

        List<Offer> savedOffers = offerRepository.saveAll(offersToSave);

        return savedOffers.stream()
                .map(OfferMapper::mapFromOfferToOfferResponseDto)
                .collect(Collectors.toList());

    }

    private List<JobOfferResponse> filterOffersWithSameUrl(List<JobOfferResponse> fetchedOffers) {
        List<String> existingUrls = offerRepository.findAll().stream()
                .map(Offer::getUrl)
                .toList();
        return fetchedOffers.stream()
                .filter(jobOfferResponse -> !existingUrls.contains(jobOfferResponse.url()))
                .collect(Collectors.toList());
    }
}
