package com.emm.msparticipants.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    @KafkaListener(topics = "participantes-eventos", groupId = "participants-group")
    public void escuchar(String mensaje) {
        log.info(">>> Evento recibido desde Kafka: {}", mensaje);
    }
}