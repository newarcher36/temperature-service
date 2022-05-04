package com.weather.temperatureservice.application.usecase;

import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

@Named
public class SaveTemperature {

    private final TemperatureRepository temperatureRepository;

    public SaveTemperature(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @Transactional
    public Temperature save(Temperature temperature) {
        return temperatureRepository.save(temperature);
    }
}
