package com.kafka.bootkafka.other;


import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * 异步提交 offset
 * 虽然同步提交 offset 更可靠一些，但是由于其会阻塞当前线程，直到提交成功。因此吞
 * 吐量会收到很大的影响。因此更多的情况下，会选用异步提交 offset 的方式。
 */
public class CustomComsumerAsynCommit {
    public static void main(String[] args) {
        Properties props = new Properties();
        //Kafka 集群
        props.put("bootstrap.servers", "127.0.0.1:9092");
        //消费者组，只要 group.id 相同，就属于同一个消费者组
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        //关闭自动提交 offset
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("first"));//消费者订阅主题
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));//消费者拉取数据
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = % s % n ", record.offset(), record.key(), record.value());
            }
            //异步提交
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition,
                        OffsetAndMetadata> offsets, Exception exception) {
                    if (exception != null) {
                        System.err.println("Commit failed for" +
                                offsets);
                    }
                }
            });
        }
    }
}