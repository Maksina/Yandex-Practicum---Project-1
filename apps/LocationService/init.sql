-- Create the database if it doesn't exist
CREATE DATABASE location_db;

-- Connect to the database
\c location_db;

-- Создание таблицы locations для управления иерархией помещений
CREATE TABLE IF NOT EXISTS locations (
    location_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    parent_location_id BIGINT REFERENCES locations(location_id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для ускорения запросов
CREATE INDEX IF NOT EXISTS idx_locations_user_id ON locations (user_id);
CREATE INDEX IF NOT EXISTS idx_locations_parent_id ON locations (parent_location_id);
