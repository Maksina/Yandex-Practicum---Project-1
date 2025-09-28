package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTypeCommandCreate {
    @NotNull
    private Long deviceTypeId;
    @NotBlank
    private String name;
}