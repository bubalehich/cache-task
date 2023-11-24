package ru.clevertec.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.util.EntityTestBuilder;
import ru.clevertec.entity.Entity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LFUCacheTest {
    private Cache<UUID, Entity> cache;

    @BeforeEach
    void init() {
        cache = new LFUCache<>(3);

        cache.put(EntityTestBuilder.anEntity().getId(),  EntityTestBuilder.anEntity().build());
        cache.put(UUID.fromString("87142b69-8eea-4a9d-a1e7-f9d62823d128"),
                EntityTestBuilder.anEntity().withId(UUID.fromString("87142b69-8eea-4a9d-a1e7-f9d62823d128")).build());
        cache.put(UUID.fromString("90e57c02-7d73-4cff-844a-2a511ee3230b"),
                EntityTestBuilder.anEntity().withId(UUID.fromString("90e57c02-7d73-4cff-844a-2a511ee3230b")).build());
    }

    @Test
    void testGetWithExistingKey() {
        var expected = EntityTestBuilder.anEntity().build();

        var actual = cache.get(EntityTestBuilder.anEntity().getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullSource
    void testGetWithNullKey(UUID argument) {
        var actual = cache.get(argument);

        assertNull(actual);
    }

    @Test
    void testGetWithNotExistingKey() {
        var actual = cache.get(EntityTestBuilder.anEntity().getNonExistingId());

        assertNull(actual);
    }

    @Test
    void testPutWithNewValue() {
        var expected = EntityTestBuilder.anEntity()
                .withId(EntityTestBuilder.anEntity().getNonExistingId())
                .build();
        cache.put(expected.getId(), expected);

        assertNull(cache.get(EntityTestBuilder.anEntity().getId()));
        assertEquals(expected, cache.get(expected.getId()));
    }

    @Test
    void testRemoveByExistingKey() {
        var entity = EntityTestBuilder.anEntity().build();

        cache.remove(entity.getId());
        var actual = cache.get(entity.getId());

        assertNull(actual);
    }

    @Test
    void testRemoveByNotExistingKey() {
        var actual = cache.remove(EntityTestBuilder.anEntity().getNonExistingId());

        assertNull(actual);
    }
}