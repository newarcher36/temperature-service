package com.weather.temperatureservice.infrastructure.amqp.listener;

import com.weather.temperatureservice.application.usecase.SaveTemperature;
import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.amqp.event.SaveTemperatureDataEvent;
import com.weather.temperatureservice.infrastructure.amqp.event.TemperatureDataSavedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

public class SaveTemperatureEventListenerTest {

    @Mock
    private SaveTemperature saveTemperature;

    @Captor
    private ArgumentCaptor<Temperature> temperatureCaptor;

    private SaveTemperatureEventListener saveTemperatureEventListener;

    @BeforeEach
    void init() {
        openMocks(this);
        saveTemperatureEventListener = new SaveTemperatureEventListener(saveTemperature);
    }

    @Test void
    saveTemperatureData() {

        Temperature expectedSavedTemperature = Temperature.builder()
                .withId(1L)
                .withMeteoDataId(1L)
                .withTemperatureValue(23f)
                .build();
        given(saveTemperature.save(any(Temperature.class))).willReturn(expectedSavedTemperature);

        SaveTemperatureDataEvent saveTemperatureDataEvent = SaveTemperatureDataEvent.builder()
                .withMeteoDataId(1L)
                .withTemperatureValue(23f)
                .build();
        TemperatureDataSavedEvent temperatureDataSavedEvent = saveTemperatureEventListener.onMessage(saveTemperatureDataEvent);

        verify(saveTemperature).save(temperatureCaptor.capture());

        assertThat(temperatureCaptor.getValue())
                .extracting(Temperature::getMeteoDataId, Temperature::getTemperatureValue)
                .containsExactly(1L, 23f);
        assertThat(temperatureDataSavedEvent)
                .isNotNull()
                .extracting(TemperatureDataSavedEvent::getId, TemperatureDataSavedEvent::getMeteoDataId, TemperatureDataSavedEvent::getTemperatureValue)
                .containsExactly(1L, 1L ,23f);
    }
}
