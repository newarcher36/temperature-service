package com.weather.temperatureservice.infrastructure.repository;

import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
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

    public void save(TemperatureEntity temperatureEntity) {
        LOGGER.info("Saving new temperature data {}", temperatureEntity);
        entityManager.persist(temperatureEntity);
    }
}
