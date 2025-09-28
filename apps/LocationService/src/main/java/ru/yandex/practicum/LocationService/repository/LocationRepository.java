package ru.yandex.practicum.LocationService.repository;


import ru.yandex.practicum.LocationService.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByUserIdAndParentIsNull(Long userId);

    Optional<Location> findByLocationId(Long locationId);

    boolean existsByLocationId(Long locationId);
}