package com.weather.temperatureservice.infrastructure.repository;

import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class TemperatureRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<TemperatureEntity> typedQuery;

    @BeforeEach
    void init() {
        openMocks(this);
    }

    @Test
    void findTemperatureDataById() {
        given(entityManager.createQuery("select t from TemperatureEntity t where t.id = :id", TemperatureEntity.class)).willReturn(typedQuery);
        given(typedQuery.setParameter("id", 1L)).willReturn(typedQuery);
        TemperatureEntity temperatureEntity = TemperatureEntity.builder().withId(1L).withTemperatureValue(23f).withMeteoDataId(1L).build();
        given(typedQuery.getSingleResult()).willReturn(temperatureEntity);

        TemperatureRepository temperatureRepository = new TemperatureRepository(entityManager);
        TemperatureEntity foundTemperatureEntity = temperatureRepository.findById(1L);

        assertThat(foundTemperatureEntity)
                .isNotNull()
                .extracting(TemperatureEntity::getId, TemperatureEntity::getTemperatureValue, TemperatureEntity::getMeteoDataId)
                .containsExactly(1L, 23f, 1L);
    }

    @Test
    void saveTemperatureData() {
        TemperatureRepository temperatureRepository = new TemperatureRepository(entityManager);
        TemperatureEntity temperatureEntity = TemperatureEntity.builder().withId(1L).withTemperatureValue(23f).withMeteoDataId(1L).build();
        temperatureRepository.save(temperatureEntity);

        verify(entityManager).persist(eq(temperatureEntity));
    }
}