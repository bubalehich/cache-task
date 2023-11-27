package ru.clevertec.service.impl;

import ru.clevertec.dto.impl.InputEntityDto;
import ru.clevertec.dto.impl.OutputEntityDto;
import ru.clevertec.entity.Entity;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.mapper.EntityMapper;
import ru.clevertec.repository.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class EntityService {
    private Repository<UUID, Entity> repository;
    private EntityMapper mapper;

    public OutputEntityDto get(UUID uuid) {
        var entity = repository.findById(uuid)
                .orElseThrow(EntityNotFoundException::new);

        return mapper.toDto(entity);
    }

    public List<OutputEntityDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public UUID create(InputEntityDto dto) {
        var entity = mapper.toEntity(dto);
        entity.setCreatedAt(Instant.now());
        entity.setModifiedAt(Instant.now());
        entity.setActive(true);

        return repository.save(entity).getId();
    }

    public void update(UUID uuid, InputEntityDto dto) {
        var entity = repository.findById(uuid).orElseThrow(EntityNotFoundException::new);

        var updatedEntity = mapper.merge(entity, dto);

        repository.save(updatedEntity);
    }

    public void delete(UUID uuid) {
        repository.delete(uuid);
    }
}
