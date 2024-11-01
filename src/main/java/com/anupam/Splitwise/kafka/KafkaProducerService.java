package com.anupam.Splitwise.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    private static final String TOPIC = "MY_TOPIC";

    public void sendMessage(String key, String value) {
        CompletableFuture<SendResult> future = kafkaTemplate.send(TOPIC, key, value);
        future.whenComplete((sendResult, throwable) -> {
            if (throwable != null) {
                //error case
                log.error("failed to send message:{}", throwable.getMessage());
            } else {
                //success case
                RecordMetadata metadata = sendResult.getRecordMetadata();
                log.info("Message sent to topic:{},partition:{},offset:{}",
                        metadata.topic(), metadata.partition(), metadata.offset());
            }
        });
    }
}
