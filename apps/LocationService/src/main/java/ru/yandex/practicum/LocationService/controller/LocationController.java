package ru.yandex.practicum.LocationService.controller;

import ru.yandex.practicum.LocationService.dto.*;
import ru.yandex.practicum.LocationService.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationWithChildrenDto>> getLocationTree(@RequestParam Long userId) {
        return ResponseEntity.ok(locationService.getLocationTree(userId));
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(locationService.createLocation(dto));
    }

    @GetMapping("/{location_id}")
    public ResponseEntity<LocationWithChildrenDto> getLocationById(@PathVariable Long location_id,
                                                                   @RequestParam(defaultValue = "0") Long userId) {

        return ResponseEntity.ok(locationService.getLocationById(location_id));
    }

    @PatchMapping("/{location_id}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable Long location_id,
                                                      @Valid @RequestBody LocationUpdateDto dto) {
        return ResponseEntity.ok(locationService.updateLocation(location_id, dto));
    }

    @DeleteMapping("/{location_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLocation(@PathVariable Long location_id) {
        locationService.deleteLocation(location_id);
    }
}