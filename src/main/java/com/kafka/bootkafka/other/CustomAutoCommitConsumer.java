package com.kafka.bootkafka.other;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class CustomAutoCommitConsumer {


    public static void main(String[] args) {
        Properties props = new Properties();
        // 定义 kakfa 服务的地址，不需要将所有 broker 指定上
        props.put("bootstrap.servers", "127.0.0.1:9092");
        // 制定 consumer group
        props.put("group.id", "test");
        // 是否自动确认 offset
        props.put("enable.auto.commit", "true");
        // 自动确认 offset 的时间间隔
        props.put("auto.commit.interval.ms", "1000");
        // key 的序列化类
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        // value 的序列化类
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        // 定义 consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 消费者订阅的 topic, 可同时订阅多个
        consumer.subscribe(Arrays.asList("topic2"));
        while (true) {
        // 读取数据，读取超时时间为 100ms
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("结果offset = %d, key = %s, value = %s%n",
                        record.offset(), record.key(), record.value());
        }
    }
}
