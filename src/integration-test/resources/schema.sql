CREATE SCHEMA temperature;

create table if not exists temperature.temperature
(
    id                serial PRIMARY KEY,
    temperature_value float(2),
    meteo_data_id     integer
)