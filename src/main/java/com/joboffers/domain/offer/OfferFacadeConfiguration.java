package com.joboffers.domain.offer;

public class OfferFacadeConfiguration {

    static OfferFacade createForTest(OfferRepository offerRepository, OfferFetcher offerFetcher) {
        OfferValidator offerValidator = new OfferValidator(offerRepository);
        OfferService offerService = new OfferService(offerFetcher, offerRepository);
        return new OfferFacade(offerValidator, offerRepository, offerService);
    }
}
