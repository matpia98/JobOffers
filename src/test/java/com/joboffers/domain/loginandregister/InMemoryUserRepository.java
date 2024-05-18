package com.joboffers.domain.loginandregister;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> db = new ConcurrentHashMap<>();
    private AtomicLong index = new AtomicLong(0);
    @Override
    public Optional<User> findByUsername(String username) {
        return db.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public User save(User user) {
        long index = this.index.getAndIncrement();
        user.setId(index);
        db.put(index, user);
        return user;
    }
}
