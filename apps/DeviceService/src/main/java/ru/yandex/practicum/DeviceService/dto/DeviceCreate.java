package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceCreate {
    @NotBlank
    private String name;
    @NotNull
    private Long locationId;
    @NotNull
    private Long deviceTypeId;
    @NotBlank
    private String serialNumber;
}