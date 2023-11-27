package ru.clevertec.repository.impl;

import ru.clevertec.entity.Entity;
import ru.clevertec.repository.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryEntityRepository implements Repository<UUID, Entity> {
    private final Map<UUID, Entity> storage = new HashMap<>();

    @Override
    public Optional<Entity> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Entity> findAll() {
        return storage.values().stream()
                .toList();
    }

    @Override
    public Entity save(Entity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("The product can't be null.");
        }

        return entity.getId() != null && storage.containsKey(entity.getId())
                ? update(entity)
                : create(entity);
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }

    private Entity create(Entity product) {
        UUID uuid;

        do {
            uuid = UUID.randomUUID();
        } while (storage.containsKey(uuid));

        product.setId(uuid);
        storage.put(uuid, product);

        return product;
    }

    private Entity update(Entity entity) {
        entity.setModifiedAt(Instant.now());
        storage.replace(entity.getId(), entity);

        return entity;
    }
}
