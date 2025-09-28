package ru.yandex.practicum.LocationService.service;

import ru.yandex.practicum.LocationService.dto.*;
import ru.yandex.practicum.LocationService.entity.Location;
import ru.yandex.practicum.LocationService.exception.InvalidLocationHierarchyException;
import ru.yandex.practicum.LocationService.exception.LocationNotFoundException;
import ru.yandex.practicum.LocationService.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public List<LocationWithChildrenDto> getLocationTree(Long userId) {
        List<Location> roots = locationRepository.findByUserIdAndParentIsNull(userId);
        return roots.stream()
                .map(this::toTreeDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public LocationDto createLocation(LocationCreateDto dto) {
        Location location = Location.builder()
                .name(dto.getName())
                .userId(dto.getUserId())
                .build();

        if (dto.getParentLocationId() != null) {
            Location parent = locationRepository.findByLocationId(dto.getParentLocationId())
                    .orElseThrow(() -> new LocationNotFoundException("Parent location not found"));
            location.setParent(parent);
        }

        Location saved = locationRepository.save(location);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public LocationWithChildrenDto getLocationById(Long locationId) {
        Location location = locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new LocationNotFoundException("Location not found"));
        return toTreeDto(location);
    }

    @Transactional
    public LocationDto updateLocation(Long locationId, LocationUpdateDto dto) {
        Location location = locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new LocationNotFoundException("Location not found"));

        if (dto.getName() != null) {
            location.setName(dto.getName());
        }

        if (dto.getParentLocationId() != null) {
            if (dto.getParentLocationId().equals(locationId)) {
                throw new InvalidLocationHierarchyException("Cannot set location as its own parent");
            }
            Location newParent = locationRepository.findByLocationId(dto.getParentLocationId())
                    .orElseThrow(() -> new LocationNotFoundException("New parent location not found"));
            if (wouldCreateCycle(location, newParent)) {
                throw new InvalidLocationHierarchyException("Changing parent creates a cycle");
            }
            location.setParent(newParent);
        } else if (dto.getParentLocationId() == null) {
            location.setParent(null);
        }

        Location updated = locationRepository.save(location);
        return toDto(updated);
    }

    private boolean wouldCreateCycle(Location child, Location potentialParent) {
        Location current = potentialParent;
        while (current != null) {
            if (current.getLocationId().equals(child.getLocationId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    @Transactional
    public void deleteLocation(Long locationId) {
        if (!locationRepository.existsByLocationId(locationId)) {
            throw new LocationNotFoundException("Location not found");
        }

        // Удаляем всё поддерево
        locationRepository.deleteById(locationId);
    }

    // Маппинг
    private LocationDto toDto(Location location) {
        if (location == null) return null;
        LocationDto dto = new LocationDto();
        dto.setLocationId(location.getLocationId());
        dto.setName(location.getName());
        dto.setUserId(location.getUserId());
        dto.setParentLocationId(location.getParent() != null ? location.getParent().getLocationId() : null);
        dto.setCreatedAt(location.getCreatedAt());
        dto.setUpdatedAt(location.getUpdatedAt());
        return dto;
    }

    private LocationWithChildrenDto toTreeDto(Location location) {
        LocationWithChildrenDto dto = new LocationWithChildrenDto();
        dto.setLocationId(location.getLocationId());
        dto.setName(location.getName());
        dto.setUserId(location.getUserId());
        dto.setParentLocationId(location.getParent() != null ? location.getParent().getLocationId() : null);
        dto.setCreatedAt(location.getCreatedAt());
        dto.setUpdatedAt(location.getUpdatedAt());

        for (Location child : location.getChildren()) {
            dto.getChildren().add(toTreeDto(child));
        }
        return dto;
    }
}