package ru.yandex.practicum.DeviceService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTypeCreate {
    @NotBlank
    private String name;
}