package ru.clevertec.cache.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import ru.clevertec.cache.Cache;
import ru.clevertec.entity.Entity;
import ru.clevertec.util.EntityTestBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LFUCacheTest {
    private Cache<UUID, Entity> cache;

    @BeforeEach
    void init() {
        cache = new LFUCache<>(3);
        EntityTestBuilder.anEntity()
                .getMapOfEntities()
                .forEach((k, v) -> cache.put(k, v));
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
                .withId(EntityTestBuilder.anEntity()
                        .getNonExistingId())
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