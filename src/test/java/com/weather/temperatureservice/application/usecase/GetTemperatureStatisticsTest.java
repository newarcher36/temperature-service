package com.weather.temperatureservice.application.usecase;

import com.weather.temperatureservice.domain.TemperatureStatistics;
import com.weather.temperatureservice.infrastructure.repository.TemperatureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

class GetTemperatureStatisticsTest {

    @Mock
    private TemperatureRepository temperatureRepository;

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

        given(temperatureRepository.calculateTemperatureStatisticsByIds(new Long[]{1L, 2L})).willReturn(expectedTemperatureStatistics);

        TemperatureStatistics actualTemperatureStatisticsEvent = new GetTemperatureStatistics(temperatureRepository).get(new Long[]{1L,2L});

        assertThat(actualTemperatureStatisticsEvent)
                .usingRecursiveComparison()
                .isEqualTo(expectedTemperatureStatistics);
    }
}