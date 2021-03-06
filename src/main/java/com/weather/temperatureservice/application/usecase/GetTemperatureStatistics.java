package com.weather.temperatureservice.application.usecase;

import com.weather.temperatureservice.domain.TemperatureStatistics;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;

import javax.inject.Named;

@Named
public class GetTemperatureStatistics {

    private final TemperatureRepository temperatureRepository;

    public GetTemperatureStatistics(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    public TemperatureStatistics get(Long[] meteoDataIds) {
        return temperatureRepository.calculateTemperatureStatisticsByIds(meteoDataIds);
    }
}
