package com.weather.temperatureservice.application.usecase;

import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named
public class SaveTemperature {

    private final TemperatureRepository temperatureRepository;

    public SaveTemperature(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @Transactional
    public void save(Temperature temperature) {
        temperatureRepository.save(map(temperature));
    }

    private TemperatureEntity map(Temperature temperature) {
        return TemperatureEntity.builder()
                .withMeteoDataId(temperature.getMeteoDataId())
                .withTemperatureValue(temperature.getTemperatureValue())
                .build();
    }
}
