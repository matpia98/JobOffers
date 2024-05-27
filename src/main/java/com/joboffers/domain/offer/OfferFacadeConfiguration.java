package com.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OfferFacadeConfiguration {


    @Bean
    OfferFacade offerFacade(OfferRepository offerRepository, OfferFetcher offerFetcher) {
        OfferValidator offerValidator = new OfferValidator(offerRepository);
        OfferService offerService = new OfferService(offerFetcher, offerRepository);
        return new OfferFacade(offerValidator, offerRepository, offerService);
    }

    static OfferFacade createForTest(OfferRepository offerRepository, OfferFetcher offerFetcher) {
        OfferValidator offerValidator = new OfferValidator(offerRepository);
        OfferService offerService = new OfferService(offerFetcher, offerRepository);
        return new OfferFacade(offerValidator, offerRepository, offerService);
    }
}
