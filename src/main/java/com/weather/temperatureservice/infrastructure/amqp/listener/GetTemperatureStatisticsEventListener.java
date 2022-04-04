package com.weather.temperatureservice.infrastructure.amqp.listener;

import com.weather.temperatureservice.application.usecase.GetTemperatureStatistics;
import com.weather.temperatureservice.domain.TemperatureStatistics;
import com.weather.temperatureservice.infrastructure.amqp.event.GetTemperatureStatisticsEvent;
import com.weather.temperatureservice.infrastructure.amqp.event.TemperatureStatisticsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class GetTemperatureStatisticsEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTemperatureStatisticsEventListener.class);
    private final GetTemperatureStatistics getTemperatureStatistics;

    public GetTemperatureStatisticsEventListener(GetTemperatureStatistics getTemperatureStatistics) {
        this.getTemperatureStatistics = getTemperatureStatistics;
    }

    @RabbitListener(queues = "${amqp.get-temperature-statistics-queue}")
    public TemperatureStatisticsEvent onMessage(GetTemperatureStatisticsEvent getTemperatureStatisticsEvent) {
        LOGGER.info("Received get temperature statistics event {}", getTemperatureStatisticsEvent);
        return map(getTemperatureStatistics.get(getTemperatureStatisticsEvent.getMeteoDataIds()));
    }

    private TemperatureStatisticsEvent map(TemperatureStatistics temperatureStatistics) {
        return TemperatureStatisticsEvent.builder()
                .withCurrentTemperature(temperatureStatistics.getCurrentTemperature())
                .withAvgTemperature(temperatureStatistics.getAvgTemperature())
                .withMaxTemperature(temperatureStatistics.getMaxTemperature())
                .withMinTemperature(temperatureStatistics.getMinTemperature())
                .build();
    }
}
