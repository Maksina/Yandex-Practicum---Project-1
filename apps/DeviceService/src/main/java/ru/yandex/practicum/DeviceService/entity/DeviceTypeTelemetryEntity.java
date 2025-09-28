package ru.yandex.practicum.DeviceService.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_type_telemetries",
        uniqueConstraints = @UniqueConstraint(columnNames = {"device_type_id", "name"}))
@Data
public class DeviceTypeTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceTypeTelemetryId;

    private Long deviceTypeId;
    private String telemetryType;
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
