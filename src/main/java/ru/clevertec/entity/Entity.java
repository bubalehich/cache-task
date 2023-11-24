package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Entity {
    private UUID id;
    private Instant createdAt;
    private Instant modifiedAt;
    private List<String> collection;
    private boolean isActive;
}
