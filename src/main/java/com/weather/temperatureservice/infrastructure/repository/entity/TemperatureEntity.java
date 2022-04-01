package com.weather.temperatureservice.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder(setterPrefix = "with")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(schema = "temperature", name = "temperature")
public class TemperatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "temperature_value", nullable = false)
    private Float temperatureValue;
    @Column(name = "meteo_data_id", nullable = false)
    private Long meteoDataId;
}
