package ru.clevertec.repository.impl;

import lombok.RequiredArgsConstructor;
import ru.clevertec.cache.Cache;
import ru.clevertec.entity.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class ProxyInMemoryRepository extends InMemoryEntityRepository {
    private InMemoryEntityRepository repository;
    private Cache<UUID, Entity> cache;

    @Override
    public Optional<Entity> findById(UUID id) {
        var entity = Optional.ofNullable(cache.get(id));

        return entity.isPresent() ? entity : repository.findById(id);
    }

    @Override
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Override
    public Entity save(Entity entity) {
        entity = repository.save(entity);
        cache.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public void delete(UUID id) {
        repository.delete(id);
        cache.remove(id);
    }
}
