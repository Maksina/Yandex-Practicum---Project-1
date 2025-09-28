package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTypeTelemetryCreate {
    @NotNull
    private Long deviceTypeId;
    @NotBlank
    private String name;
    @NotBlank
    private String telemetryType;
}