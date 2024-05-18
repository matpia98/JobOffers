package com.joboffers.domain.offer;

import com.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;

public class InMemoryOfferFetcher implements OfferFetcher{

    private final List<JobOfferResponse> offersToFetch;

    InMemoryOfferFetcher(List<JobOfferResponse> offersToFetch) {
        this.offersToFetch = offersToFetch;
    }

    @Override
    public List<JobOfferResponse> fetchOffers() {
        return offersToFetch;
    }
}
