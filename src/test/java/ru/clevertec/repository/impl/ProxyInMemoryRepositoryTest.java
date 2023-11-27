package ru.clevertec.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cache.Cache;
import ru.clevertec.entity.Entity;
import ru.clevertec.util.EntityTestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyInMemoryRepositoryTest {
    @InjectMocks
    private ProxyInMemoryRepository repository;

    @Mock
    private InMemoryEntityRepository inMemoryEntityRepository;

    @Mock
    private Cache<UUID, Entity> cache;

    @Test
    void testFindByIdShouldCallCache() {
        var expected = EntityTestBuilder.anEntity().build();
        when(cache.get(expected.getId())).thenReturn(expected);

        var actual = repository.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(cache).get(expected.getId());
        verify(inMemoryEntityRepository, never()).findById(expected.getId());
    }

    @Test
    void testFindByIdShouldCallRepository() {
        var expected = EntityTestBuilder.anEntity().build();
        when(cache.get(expected.getId())).thenReturn(null);
        when(inMemoryEntityRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        var inOrder = Mockito.inOrder(cache, inMemoryEntityRepository);

        var actual = repository.findById(expected.getId());

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        inOrder.verify(cache).get(expected.getId());
        inOrder.verify(inMemoryEntityRepository).findById(expected.getId());
    }

    @Test
    void testFindAll() {
        var expected = List.of(EntityTestBuilder.anEntity().build());
        when(inMemoryEntityRepository.findAll()).thenReturn(expected);

        var actual = repository.findAll();

        assertEquals(expected, actual);
        verify(inMemoryEntityRepository).findAll();
    }

    @Test
    void testSave() {
        var expected = EntityTestBuilder.anEntity().build();
        when(inMemoryEntityRepository.save(expected)).thenReturn(expected);
        doNothing().when(cache).put(expected.getId(), expected);
        InOrder inOrder = Mockito.inOrder(inMemoryEntityRepository, cache);

        var actual = repository.save(expected);

        assertEquals(expected, actual);
        inOrder.verify(inMemoryEntityRepository).save(expected);
        inOrder.verify(cache).put(expected.getId(), expected);
    }

    @Test
    void delete() {
        var id = EntityTestBuilder.anEntity().getId();
        doNothing().when(inMemoryEntityRepository).delete(id);
        when(cache.remove(id)).thenReturn(any(Entity.class));
        InOrder inOrder = Mockito.inOrder(inMemoryEntityRepository, cache);

        repository.delete(id);

        inOrder.verify(inMemoryEntityRepository).delete(id);
        inOrder.verify(cache).remove(id);
    }
}