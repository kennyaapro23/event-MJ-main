package com.emm.msparticipants.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    private static final Logger log = LoggerFactory.getLogger(EventProducer.class);

    public static final String TOPIC = "participantes-eventos";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public EventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicar(String mensaje) {
        kafkaTemplate.send(TOPIC, mensaje).whenComplete((result, ex) -> {
            if (ex != null) {
                log.warn("No se pudo publicar en Kafka: {}", ex.getMessage());
            }
        });
    }
}
