package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.dto.impl.InputEntityDto;
import ru.clevertec.dto.impl.OutputEntityDto;
import ru.clevertec.entity.Entity;

@Mapper
public interface EntityMapper extends MappersConfig {
    Entity toEntity(InputEntityDto dto);

    OutputEntityDto toDto(Entity entity);

    Entity merge(@MappingTarget Entity entity, InputEntityDto dto);
}
