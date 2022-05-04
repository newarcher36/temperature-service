package com.weather.temperatureservice.infrastructure.repository;

import com.weather.temperatureservice.domain.Temperature;
import com.weather.temperatureservice.infrastructure.repository.entity.TemperatureEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class TemperatureRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<TemperatureEntity> typedQuery;

    @Captor
    private ArgumentCaptor<TemperatureEntity> temperatureEntityCaptor;

    private TemperatureRepository temperatureRepository;

    @BeforeEach
    void init() {
        openMocks(this);
        temperatureRepository = new TemperatureRepository(entityManager);
    }

    @Test
    void findTemperatureDataById() {
        given(entityManager.createQuery("select t from TemperatureEntity t where t.id = :id", TemperatureEntity.class)).willReturn(typedQuery);
        given(typedQuery.setParameter("id", 1L)).willReturn(typedQuery);
        TemperatureEntity temperatureEntity = TemperatureEntity.builder()
                .withId(1L)
                .withTemperatureValue(23f)
                .withMeteoDataId(1L)
                .build();
        given(typedQuery.getSingleResult()).willReturn(temperatureEntity);

        TemperatureEntity foundTemperatureEntity = temperatureRepository.findById(1L);

        assertThat(foundTemperatureEntity)
                .isNotNull()
                .extracting(TemperatureEntity::getId, TemperatureEntity::getTemperatureValue, TemperatureEntity::getMeteoDataId)
                .containsExactly(1L, 23f, 1L);
    }

    @Test
    void saveTemperatureData() {

        TemperatureEntity savedTemperatureEntity = TemperatureEntity.builder()
                .withId(1L)
                .withMeteoDataId(1L)
                .withTemperatureValue(23f)
                .build();
        given(entityManager.merge(any(TemperatureEntity.class))).willReturn(savedTemperatureEntity);

        Temperature temperature = Temperature.builder()
                .withMeteoDataId(1L)
                .withTemperatureValue(23f)
                .build();
        Temperature savedTemperature = temperatureRepository.save(temperature);

        verify(entityManager).merge(temperatureEntityCaptor.capture());

        assertThat(temperatureEntityCaptor.getValue())
                .extracting(TemperatureEntity::getMeteoDataId, TemperatureEntity::getTemperatureValue)
                .containsExactly(1L, 23f);

        assertThat(savedTemperature)
                .extracting(Temperature::getId, Temperature::getMeteoDataId, Temperature::getTemperatureValue)
                .containsExactly(1L, 1L, 23f);
    }
}