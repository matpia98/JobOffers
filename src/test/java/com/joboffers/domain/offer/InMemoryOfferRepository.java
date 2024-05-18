package com.joboffers.domain.offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class InMemoryOfferRepository implements OfferRepository {

    private Map<Long, Offer> db = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(0);
    @Override
    public Offer save(Offer offerToSave) {
        long index = this.index.getAndIncrement();
        offerToSave.setId(index);
        db.put(index, offerToSave);
        return offerToSave;
    }

    @Override
    public List<Offer> saveAll(List<Offer> offersToSave) {
        offersToSave.forEach(
                this::save
        );
        return offersToSave;
    }

    @Override
    public Optional<Offer> findById(Long id) {
        Offer offer = db.get(id);
        return Optional.ofNullable(offer);
    }

    @Override
    public List<Offer> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Optional<Offer> findByUrl(String url) {
        return db.values()
                .stream()
                .filter(offer -> offer.getUrl().equals(url))
                .findFirst();
    }
}
