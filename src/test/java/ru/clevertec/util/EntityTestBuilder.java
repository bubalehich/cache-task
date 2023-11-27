package ru.clevertec.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.dto.impl.InputEntityDto;
import ru.clevertec.dto.impl.OutputEntityDto;
import ru.clevertec.entity.Entity;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(staticName = "anEntity")
@AllArgsConstructor
@With
@Getter
public class EntityTestBuilder {
    private UUID id = UUID.fromString("5f1d03c6-c7e2-44b5-ae1f-879347c01893");
    private Instant createdAt = Instant.now();
    private Instant modifiedAt = Instant.now();
    private List<String> collection = List.of("we", "butter", "the", "bread", "with", "butter");
    private boolean isActive = true;

    public Entity build() {
        return Entity.builder()
                .id(id)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .collection(collection)
                .isActive(isActive)
                .build();
    }

    public InputEntityDto getInputEntityDto() {
        return InputEntityDto.builder()
                .isActive(isActive)
                .collection(collection)
                .build();
    }

    public OutputEntityDto getOutputEntityDto() {
        return OutputEntityDto.builder()
                .id(id)
                .isActive(isActive)
                .collection(collection)
                .build();
    }

    public UUID getNonExistingId() {
        return UUID.fromString("87142b69-8eea-4a9d-a1e7-f9d62823d188");
    }

    public LinkedHashMap<UUID, Entity> getMapOfEntities() {
        var map = new LinkedHashMap<UUID, Entity>();
        map.put(id, EntityTestBuilder.anEntity().build());
        map.put(UUID.fromString("87142b69-8eea-4a9d-a1e7-f9d62823d128"), EntityTestBuilder.anEntity().withId(UUID.fromString("87142b69-8eea-4a9d-a1e7-f9d62823d128")).build());
        map.put(UUID.fromString("90e57c02-7d73-4cff-844a-2a511ee3230b"), EntityTestBuilder.anEntity().withId(UUID.fromString("90e57c02-7d73-4cff-844a-2a511ee3230b")).build());

        return map;
    }
}
