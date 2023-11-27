package ru.clevertec.dto.impl;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.dto.OutputDto;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OutputEntityDto implements OutputDto {
    private UUID id;
    private boolean isActive;
    private List<String> collection;
}
