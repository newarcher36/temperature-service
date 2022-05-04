package com.weather.temperatureservice.application.usecase;

import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class SaveTemperatureTest {

    @Mock
    private TemperatureRepository temperatureRepository;

    @Captor
    private ArgumentCaptor<Temperature> temperatureEntityCaptor;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test
    void saveTemperature() {
        SaveTemperature saveTemperature = new SaveTemperature(temperatureRepository);
        Temperature temperature = Temperature.builder()
                .withMeteoDataId(1L)
                .withTemperatureValue(23f)
                .build();
        saveTemperature.save(temperature);

        verify(temperatureRepository).save(temperatureEntityCaptor.capture());

        assertThat(temperatureEntityCaptor.getValue())
                .extracting(Temperature::getMeteoDataId, Temperature::getTemperatureValue)
                .containsExactly(1L, 23f);
    }
}