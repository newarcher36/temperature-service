package com.weather.temperatureservice.infrastructure.repository;

import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.domain.TemperatureStatistics;
import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.persistence.EntityManager;

import static java.util.Arrays.asList;

@Named
public class TemperatureRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperatureRepository.class);
    private final EntityManager entityManager;

    public TemperatureRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TemperatureEntity findById(long id) {
        LOGGER.info("Find temperature data by id={}", id);
        return entityManager.createQuery("select t from TemperatureEntity t where t.id = :id", TemperatureEntity.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public TemperatureStatistics calculateTemperatureStatisticsByIds(Long[] meteoDataIds) {
        LOGGER.info("Calculating temperature statistics from {}", meteoDataIds);

        String currentTemperatureQuery = "select t.temperatureValue from TemperatureEntity t order by t.id desc";
        Float currentTemperature = entityManager.createQuery(currentTemperatureQuery, Float.class)
                .setMaxResults(1)
                .getSingleResult();

        String temperatureStatisticsQuery = "select max(t.temperatureValue) as maxTemperature, " +
                "min(t.temperatureValue) as minTemperature, " +
                "trunc(avg(t.temperatureValue)) as avgTemperature " +
                "from TemperatureEntity t " +
                "where t.meteoDataId in :ids";
        Object[] temperatureStatistics = entityManager.createQuery(temperatureStatisticsQuery, Object[].class).setParameter("ids", asList(meteoDataIds)).getSingleResult();

        return TemperatureStatistics.builder()
                .withCurrentTemperature(currentTemperature)
                .withMaxTemperature((Float) temperatureStatistics[0])
                .withMinTemperature((Float) temperatureStatistics[1])
                .withAvgTemperature(((Number) temperatureStatistics[2]).floatValue())
                .build();
    }

    public Temperature save(Temperature temperature) {
        LOGGER.info("Saving new temperature data {}", temperature);
        TemperatureEntity temperatureEntity = mapToTemperatureEntity(temperature);
        return mapToTemperature(entityManager.merge(temperatureEntity));
    }
    private TemperatureEntity mapToTemperatureEntity(Temperature temperature) {
        return TemperatureEntity.builder()
                .withMeteoDataId(temperature.getMeteoDataId())
                .withTemperatureValue(temperature.getTemperatureValue())
                .build();
    }

    private Temperature mapToTemperature(TemperatureEntity temperatureEntity) {
        return Temperature.builder()
                .withId(temperatureEntity.getId())
                .withMeteoDataId(temperatureEntity.getMeteoDataId())
                .withTemperatureValue(temperatureEntity.getTemperatureValue())
                .build();
    }
}
