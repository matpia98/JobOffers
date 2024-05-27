package com.joboffers.domain.offer;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Log4j2
class InMemoryOfferRepository implements OfferRepository {

    private Map<Long, Offer> db = new ConcurrentHashMap<>();
    private AtomicInteger index = new AtomicInteger(0);


    @Override
    public Optional<Offer> findById(String id) {
        try {
            long idAsLong = Long.parseLong(id);
            Offer offer = db.get(idAsLong);
            return Optional.ofNullable(offer);
        } catch (NumberFormatException e) {
            log.warn("Number Format Exception when parsing string to long " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean existsById(String id) {
        try {
            long idAsLong = Long.parseLong(id);
            return db.containsKey(idAsLong);
        } catch (NumberFormatException e) {
            log.warn("Number Format Exception when parsing string to long " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<Offer> findByUrl(String url) {
        return db.values()
                .stream()
                .filter(offer -> offer.getUrl().equals(url))
                .findFirst();
    }

    @Override
    public <S extends Offer> S save(S entity) {
        long index = this.index.getAndIncrement();
        String indexString = String.valueOf(index);
        Offer offer = new Offer(
                indexString,
                entity.getUrl(),
                entity.getPosition(),
                entity.getCompanyName(),
                entity.getSalary()
        );
        db.put(index, offer);
        return (S) offer;
    }

    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        entities.forEach(entity -> {
            S savedEntity = save(entity);
            savedEntities.add(savedEntity);
        });
        return savedEntities;
    }

    @Override
    public List<Offer> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public List<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }


    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }
}
