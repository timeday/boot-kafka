package com.kafka.bootkafka.producter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaSendResultHandler producerListener;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 向topic中发送消息
     */
    public void send (String topic, String msg) {
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send(topic, msg);
    }

    /**
     * 向topic中发送消息
     */
    public void send (String topic, List<String> msgs) {
        msgs.forEach(msg -> kafkaTemplate.send(topic, msg));
    }
}
