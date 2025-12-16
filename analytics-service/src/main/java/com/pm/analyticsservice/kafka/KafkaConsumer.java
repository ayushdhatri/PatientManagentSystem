package com.pm.analyticsservice.kafka;


import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);
    @KafkaListener(topics = "patient", groupId = "analytics-service-v2")
    public void consumeEvent(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                             @Payload byte[] payload) {
        try {
            log.info("Processing message with key: {}", key);

            PatientEvent patientEvent = PatientEvent.parseFrom(payload);

            log.info("✅ Received Patient Event : [PatientId : {}, PatientName : {}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName()
            );
        } catch (InvalidProtocolBufferException e) {
            log.error("❌ Deserialization failed for key {}: {}", key, e.getMessage());
        }
    }

}
