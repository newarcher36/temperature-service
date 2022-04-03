package com.weather.temperatureservice.infrastructure.amqp.listener;

import com.weather.temperatureservice.application.usecase.GetTemperatureStatistics;
import com.weather.temperatureservice.domain.TemperatureStatistics;
import com.weather.temperatureservice.infrastructure.amqp.event.GetTemperatureStatisticsEvent;
import com.weather.temperatureservice.infrastructure.amqp.event.TemperatureStatisticsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

public class GetTemperatureEventListenerTest {

    @Mock
    private GetTemperatureStatistics getTemperatureStatistics;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test void
    getTemperatureStatistics() {

        TemperatureStatistics expectedTemperatureStatistics = TemperatureStatistics.builder()
                .withCurrentTemperature(15f)
                .withAvgTemperature(20f)
                .withMaxTemperature(30f)
                .withMinTemperature(10f)
                .build();

        given(getTemperatureStatistics.get(new Long[]{1L,2L})).willReturn(expectedTemperatureStatistics);

        GetTemperatureStatisticsEvent getTemperatureStatisticsEvent = GetTemperatureStatisticsEvent.builder()
                .withMeteoDataIds(new Long[]{1L,2L})
                .build();

        TemperatureStatisticsEvent actualTemperatureStatisticsEvent = new GetTemperatureDataEventListener(getTemperatureStatistics).onMessage(getTemperatureStatisticsEvent);

        assertThat(actualTemperatureStatisticsEvent)
                .usingRecursiveComparison()
                .isEqualTo(expectedTemperatureStatistics);
    }
}
