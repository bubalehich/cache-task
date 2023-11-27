package ru.clevertec.dto.impl;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.dto.InputDto;

import java.util.List;

@Data
@Builder
public class InputEntityDto implements InputDto {
    private List<String> collection;
    private boolean isActive;
}
