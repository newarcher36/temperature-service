package com.weather.temperatureservice.application.usecase;

import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.BDDMockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class SaveTemperatureTest {

    @Mock
    private TemperatureRepository temperatureRepository;

    @Captor
    private ArgumentCaptor<TemperatureEntity> temperatureEntityCaptor;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test
    void saveTemperature() {
        SaveTemperature saveTemperature = new SaveTemperature(temperatureRepository);
        Temperature temperature = Temperature.builder().withMeteoDataId(1L).withTemperatureValue(23f).build();
        saveTemperature.save(temperature);

        verify(temperatureRepository).save(temperatureEntityCaptor.capture());

        Assertions.assertThat(temperatureEntityCaptor.getValue())
                .extracting(TemperatureEntity::getMeteoDataId, TemperatureEntity::getTemperatureValue)
                .containsExactly(1L, 23f);
    }
}