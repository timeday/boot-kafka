package com.kafka.bootkafka.customer;

import com.alibaba.fastjson.JSON;
import com.kafka.bootkafka.common.Contants;
import com.kafka.bootkafka.common.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Component
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 监听topic1主题,单条消费
     * 如使用@EnableKafka可以监听AbstractListenerContainerFactory子类目标端点，
     * 如ConcurrentKafkaListenerContainerFactory是AbstractKafkaListenerContainerFactory的子类
     */
    @KafkaListener(topics = "topic1")
    public void listen0(ConsumerRecord<String, String> record) {
        consumer(record);
    }

    /**
     * 监听topic2主题,单条消费
     */
    @KafkaListener(topics = "${topicName.topic2}")
    public void listen1(ConsumerRecord<String, String> record) {
        consumer(record);
    }

    /**
     * 监听topic3和topic4,单条消费
     */
    @KafkaListener(topics = {"topic3", "topic4"})
    public void listen2(ConsumerRecord<String, String> record) {
        consumer(record);
    }

    /**
     * 监听topic5,批量消费
     */
    @KafkaListener(topics = "${topicName.topic2}", containerFactory = "batchFactory")
    public void listen2(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        batchConsumer(records, ack);
    }

    /**
     * 单条消费
     */
    public void consumer(ConsumerRecord<String, String> record) {
        logger.debug("主题:{}, 内容: {}", record.topic(), record.value());
    }

    /**
     * 批量消费
     */
    public void batchConsumer(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        records.forEach(record -> consumer(record));

        List<User> userList = new ArrayList<>();
        try {
            records.forEach(record -> {
                User user = JSON.parseObject(record.value().toString(), User.class);
                user.getCreateTime().format(DateTimeFormatter.ofPattern(Contants.DateTimeFormat.DATE_TIME_PATTERN));
                userList.add(user);
            });
        } catch (Exception e) {
        } finally {
            //ack.acknowledge();//手动提交偏移量
        }

    }
}