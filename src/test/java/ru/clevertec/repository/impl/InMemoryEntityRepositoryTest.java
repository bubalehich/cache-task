package ru.clevertec.repository.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.util.EntityTestBuilder;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryEntityRepositoryTest {
    private InMemoryEntityRepository repository;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        repository = new InMemoryEntityRepository();

        var data = EntityTestBuilder.anEntity().getMapOfEntities();

        Field field = repository.getClass().getDeclaredField("storage");
        field.setAccessible(true);
        field.set(repository, data);
    }

    @Test
    void testFindAllShouldReturnCollection() {
        var expected = EntityTestBuilder.anEntity()
                .getMapOfEntities()
                .values().stream()
                .toList();

        var actual = repository.findAll();

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testFindByIdShouldReturnProduct() {
        var expected = EntityTestBuilder.anEntity().build();

        var actual = repository.findById(expected.getId()).orElseThrow();

        assertEquals(expected, actual);
    }

    @Test
    void testFindByIdShouldReturnOptionalOfEmpty() {
        var expected = Optional.empty();

        var actual = repository.findById(EntityTestBuilder.anEntity().getNonExistingId());

        assertEquals(expected, actual);
    }

    @Test
    void testSaveShouldReturnProductWithUuid() {
        var expected = EntityTestBuilder.anEntity()
                .withId(null)
                .build();

        var actual = repository.save(expected);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertNotNull(actual.getId())
        );
    }

    @Test
    void testSaveWithNullArgumentShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void testSaveShouldUpdateProduct() {
        var expected = EntityTestBuilder.anEntity()
                .withActive(false)
                .withCollection(null)
                .build();

        var actual = repository.save(expected);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected.isActive(), actual.isActive()),
                () -> assertEquals(expected.getCreatedAt(), actual.getCreatedAt())
        );
    }

    @Test
    void testDeleteShouldDeleteProduct() {
        repository.delete(EntityTestBuilder.anEntity().getId());

        assertFalse(repository.findById(EntityTestBuilder.anEntity().getId()).isPresent());
    }
}