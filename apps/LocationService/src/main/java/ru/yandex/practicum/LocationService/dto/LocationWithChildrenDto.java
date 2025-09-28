package ru.yandex.practicum.LocationService.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationWithChildrenDto extends LocationDto {
    private List<LocationWithChildrenDto> children = new ArrayList<>();
}