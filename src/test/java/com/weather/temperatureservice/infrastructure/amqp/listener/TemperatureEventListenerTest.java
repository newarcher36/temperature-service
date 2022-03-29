package com.weather.temperatureservice.infrastructure.amqp.listener;

import com.weather.temperatureservice.application.usecase.SaveTemperature;
import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.amqp.event.TemperatureDataEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class TemperatureEventListenerTest {

    @Mock
    private SaveTemperature saveTemperature;

    @Captor
    private ArgumentCaptor<Temperature> temperatureCaptor;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test void
    saveTemperatureData() {
        TemperatureEventListener temperatureEventListener = new TemperatureEventListener(saveTemperature);
        TemperatureDataEvent temperatureDataEvent = TemperatureDataEvent.builder().withMeteoDataId(1L).withTemperatureValue(23f).build();

        temperatureEventListener.listenTemperatureDataEvents(temperatureDataEvent);


        verify(saveTemperature).save(temperatureCaptor.capture());

        assertThat(temperatureCaptor.getValue())
                .extracting(Temperature::getMeteoDataId, Temperature::getTemperatureValue)
                .containsExactly(1L, 23f);
    }
}