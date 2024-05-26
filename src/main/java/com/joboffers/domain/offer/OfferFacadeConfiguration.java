package com.joboffers.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Configuration
public class OfferFacadeConfiguration {

    @Bean
    OfferRepository offerRepository() {
        return new OfferRepository() {
            private final Map<Long, Offer> offers = new HashMap<>();
            final AtomicLong currentId = new AtomicLong(0);

            @Override
            public Offer save(Offer offerToSave) {
                long newId = this.currentId.getAndIncrement();
                Offer newOffer = new Offer(newId, offerToSave.getUrl(), offerToSave.getJobTitle(),
                        offerToSave.getCompanyName(), offerToSave.getSalary());
                offers.put(newId, newOffer);
                return newOffer;
            }

            @Override
            public List<Offer> saveAll(List<Offer> offersToSave) {
                return offersToSave.stream()
                        .map(this::save)
                        .collect(Collectors.toList());
            }

            @Override
            public Optional<Offer> findById(Long id) {
                return Optional.ofNullable(offers.get(id));
            }

            @Override
            public List<Offer> findAll() {
                return new ArrayList<>(offers.values());
            }

            @Override
            public Optional<Offer> findByUrl(String url) {
                return offers.values().stream()
                        .filter(offer -> offer.getUrl().equals(url))
                        .findFirst();
            }
        };
    }


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
