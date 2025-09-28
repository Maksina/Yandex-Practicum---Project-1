package ru.yandex.practicum.LocationService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationCreateDto {
    @NotNull
    private Long userId;
    @NotNull
    private String name;
    private Long parentLocationId; // nullable
}