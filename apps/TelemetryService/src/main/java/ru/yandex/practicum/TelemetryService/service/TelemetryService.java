package ru.yandex.practicum.TelemetryService.service;

import ru.yandex.practicum.TelemetryService.exception.BadRequestException;
import ru.yandex.practicum.TelemetryService.exception.DeviceNotFoundException;
import ru.yandex.practicum.TelemetryService.model.TelemetryPoint;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.influxdb.query.FluxRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelemetryService {

    private final InfluxDBClient influxDBClient;
    private final String bucket;

    private static final Set<String> VALID_FIELDS = Set.of("temperature", "humidity", "pressure");
    private static final Set<String> VALID_AGG = Set.of("none", "mean", "max", "min", "count");
    private static final Pattern WINDOW_PATTERN = Pattern.compile("^\\d+(ms|s|m|h|d)$");

    public TelemetryService(InfluxDBClient influxDBClient,
                            @Value("${influxdb.bucket}") String bucket) {
        this.influxDBClient = influxDBClient;
        this.bucket = bucket;
    }

    public List<TelemetryPoint> getTelemetry(
            Long deviceId,
            Instant start,
            Instant stop,
            List<String> fields,
            String agg,
            String window) {

        // Валидация параметров
        if (start != null && stop != null && start.isAfter(stop)) {
            throw new BadRequestException("start must be <= stop");
        }

        if (agg == null) agg = "none";
        if (!VALID_AGG.contains(agg)) {
            throw new BadRequestException("Invalid agg value");
        }

        if (fields == null || fields.isEmpty()) {
            fields = new ArrayList<>(VALID_FIELDS);
        } else {
            for (String f : fields) {
                if (!VALID_FIELDS.contains(f)) {
                    throw new BadRequestException("Invalid field: " + f);
                }
            }
        }

        if ("none".equals(agg)) {
            window = null;
        } else {
            if (window == null) window = "1m";
            if (!WINDOW_PATTERN.matcher(window).matches()) {
                throw new BadRequestException("Invalid window format");
            }
        }

        // Установка временных границ по умолчанию
        Instant now = Instant.now();
        if (start == null) start = now.minus(1, ChronoUnit.HOURS);
        if (stop == null) stop = now;

        // Построение Flux-запроса
        StringBuilder flux = new StringBuilder();
        flux.append("from(bucket: \"").append(bucket).append("\")\n")
                .append("  |> range(start: ").append(start.toEpochMilli()).append("ms, stop: ").append(stop.toEpochMilli()).append("ms)\n")
                .append("  |> filter(fn: (r) => r._measurement == \"telemetry\" and r.device_id == \"").append(deviceId).append("\")\n");

        // Фильтрация по полям
        String fieldFilter = fields.stream()
                .map(f -> "r._field == \"" + f + "\"")
                .collect(Collectors.joining(" or "));
        flux.append("  |> filter(fn: (r) => ").append(fieldFilter).append(")\n");

        if (!"none".equals(agg)) {
            flux.append("  |> aggregateWindow(every: ").append(window).append(", fn: ").append(agg).append(", createEmpty: false)\n");
        }

        flux.append("  |> pivot(rowKey: [\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\")\n");

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables;
        try {
            tables = queryApi.query(flux.toString());
        } catch (Exception e) {
            throw new RuntimeException("InfluxDB query error", e);
        }

        if (tables.isEmpty()) {
            // Проверим, существует ли вообще устройство?
            // Для этого сделаем простой запрос без полей
            String checkQuery = String.format(
                    "from(bucket: \"%s\") |> range(start: -30d) |> filter(fn: (r) => r._measurement == \"telemetry\" and r.device_id == \"%d\") |> limit(n: 1)",
                    bucket, deviceId
            );
            List<FluxTable> check = queryApi.query(checkQuery);
            if (check.isEmpty()) {
                throw new DeviceNotFoundException("Device with id " + deviceId + " not found");
            }
            return Collections.emptyList();
        }

        List<TelemetryPoint> result = new ArrayList<>();
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                TelemetryPoint point = new TelemetryPoint();
                point.setTime(record.getTime());

                if (fields.contains("temperature")) {
                    point.setTemperature((Float) record.getValueByKey("temperature"));
                }
                if (fields.contains("humidity")) {
                    point.setHumidity((Float) record.getValueByKey("humidity"));
                }
                if (fields.contains("pressure")) {
                    point.setPressure((Float) record.getValueByKey("pressure"));
                }

                result.add(point);
            }
        }

        return result;
    }
}