package com.weather.temperatureservice.feature.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQTestConfig {

//    @Value("${spring.rabbitmq.template.default-receive-queue}")
//    private String temperatureDataQueueName;
//
//    @Value("${spring.rabbitmq.template.exchange}")
//    private String temperatureDataExchange;
//
//    @Value("${spring.rabbitmq.template.routing-key}")
//    private String temperatureDataRoutingKey;
//
//    @Value("${amqp.dlq}")
//    private String temperatureDataDlq;
//
//    @Bean
//    public Queue queue() {
//        return QueueBuilder.durable(temperatureDataQueueName)
//                .withArgument("x-dead-letter-exchange","")
//                .withArgument("x-dead-letter-routing-key", temperatureDataDlq)
//                .build();
//    }
//
//    @Bean
//    public DirectExchange exchange() {
//        return new DirectExchange(temperatureDataExchange);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder
//                .bind(queue)
//                .to(exchange)
//                .with(temperatureDataRoutingKey);
//    }
//
//    @Bean
//    public Queue deadLetterQueue() {
//        return QueueBuilder.durable(temperatureDataDlq).build();
//    }
}