package ru.yandex.practicum.LocationService.dto;

import lombok.Data;

@Data
public class LocationUpdateDto {
    private String name;
    private Long parentLocationId; // nullable
}