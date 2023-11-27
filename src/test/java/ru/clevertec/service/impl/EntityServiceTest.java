package ru.clevertec.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.dto.impl.InputEntityDto;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.mapper.EntityMapper;
import ru.clevertec.repository.impl.InMemoryEntityRepository;
import ru.clevertec.util.EntityTestBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityServiceTest {
    @Mock
    private EntityMapper mapper;

    @Mock
    private InMemoryEntityRepository repository;

    @InjectMocks
    private EntityService service;

    @Test
    void testGetShouldReturnInfoProductDto() {
        var id = EntityTestBuilder.anEntity().getId();
        var expected = EntityTestBuilder.anEntity().getOutputEntityDto();
        when(repository.findById(id)).thenReturn(Optional.of(EntityTestBuilder.anEntity().build()));
        when(mapper.toDto(EntityTestBuilder.anEntity().build())).thenReturn(expected);

        var actual = service.get(id);

        assertAll(
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
        verify(repository).findById(id);
        verify(mapper).toDto(EntityTestBuilder.anEntity().build());
    }
    @Test
    void testGetWithNonExistingUuidShouldThrowAnException() {
        var uuid = EntityTestBuilder.anEntity().getNonExistingId();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.get(uuid));
    }

    @Test
    void testGetAllShouldReturnList() {
        var uuid = EntityTestBuilder.anEntity();
        var dtoList = List.of(EntityTestBuilder.anEntity().getOutputEntityDto());
        var entityList = List.of(EntityTestBuilder.anEntity().build());
        when(repository.findAll()).thenReturn(entityList);
        when(mapper.toDto(entityList.get(0))).thenReturn(dtoList.get(0));

        var result = service.getAll();

        assertNotNull(result);
        assertEquals(dtoList, result);
    }

    @Test
    void testCreateShouldReturnEntity() {
        var dto = EntityTestBuilder.anEntity().getInputEntityDto();
        var entity = EntityTestBuilder.anEntity()
                .withModifiedAt(null)
                .withCreatedAt(null)
                .withId(null)
                .build();
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(EntityTestBuilder.anEntity().build());

        var actual = service.create(dto);

        assertNotNull(actual);
        verify(repository).save(entity);
        verify(mapper).toEntity(dto);
    }

    @Test
    void testUpdateSuccess() {
        var entity = EntityTestBuilder.anEntity().build();
        var dto = InputEntityDto.builder()
                .collection(null)
                .isActive(false)
                .build();

        var expected = EntityTestBuilder.anEntity()
                .withCollection(null)
                .withActive(false)
                .withModifiedAt(Instant.now())
                .build();
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(mapper.merge(entity, dto)).thenReturn(expected);

        service.update(entity.getId(), dto);

        verify(repository).findById(entity.getId());
        verify(mapper).merge(entity, dto);
    }

    @Test
    void testUpdateWithNonExistingIdShouldThrowAnException(){
        var uuid = EntityTestBuilder.anEntity().getNonExistingId();
        var dto = EntityTestBuilder.anEntity().getInputEntityDto();

        assertThrows(EntityNotFoundException.class,
                () -> service.update(uuid, dto));
    }

    @Test
    void testDelete() {
        var uuid = EntityTestBuilder.anEntity().getId();
        doNothing().when(repository).delete(uuid);

        service.delete(uuid);

        verify(repository).delete(uuid);
    }
}