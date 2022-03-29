package com.weather.temperatureservice.infrastructure.repository;

import com.weather.temperatureservice.infrastructure.repository.entity.Temperature;
import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.springframework.stereotype.Component;

@Component
public class TemperatureRepository {
    public Temperature findById(long l) {
        return null;
    }

    public void save(TemperatureEntity capture) {

    }
}
