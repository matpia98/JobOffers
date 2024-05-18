package com.joboffers.domain.offer;

import java.util.List;
import java.util.Optional;

interface OfferRepository {
    Offer save(Offer offerToSave);

    List<Offer> saveAll(List<Offer> offersToSave);

    Optional<Offer> findById(Long id);

    List<Offer> findAll();
    Optional<Offer> findByUrl(String url);
}
