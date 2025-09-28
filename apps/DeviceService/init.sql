-- Create the database if it doesn't exist
CREATE DATABASE device_db;

-- Connect to the database
\c device_db;

-- Создание таблицы типов устройств
CREATE TABLE IF NOT EXISTS device_types (
    device_type_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы устройств
CREATE TABLE IF NOT EXISTS devices (
    device_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location_id BIGINT NOT NULL,
    device_type_id BIGINT NOT NULL REFERENCES device_types(device_type_id) ON DELETE RESTRICT,
    is_active BOOLEAN DEFAULT true,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание таблицы команд типов устройств
CREATE TABLE IF NOT EXISTS device_type_commands (
    device_type_command_id BIGSERIAL PRIMARY KEY,
    device_type_id BIGINT NOT NULL REFERENCES device_types(device_type_id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (device_type_id, name)
);

-- Создание таблицы телеметрии типов устройств
CREATE TABLE IF NOT EXISTS device_type_telemetries (
    device_type_telemetry_id BIGSERIAL PRIMARY KEY,
    device_type_id BIGINT NOT NULL REFERENCES device_types(device_type_id) ON DELETE CASCADE,
    telemetry_type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (device_type_id, name)
);

-- Индексы для ускорения запросов (опционально, но рекомендуется)
CREATE INDEX IF NOT EXISTS idx_devices_location_id ON devices(location_id);
CREATE INDEX IF NOT EXISTS idx_commands_device_type_id ON device_type_commands(device_type_id);
CREATE INDEX IF NOT EXISTS idx_telemetries_device_type_id ON device_type_telemetries(device_type_id);

-- Предзаполнение таблицы типов устройств
INSERT INTO device_types (name) VALUES
  ('Temperature Sensor'),
  ('Humidity Sensor'),
  ('Smart Light'),
  ('Motion Detector')
ON CONFLICT DO NOTHING;