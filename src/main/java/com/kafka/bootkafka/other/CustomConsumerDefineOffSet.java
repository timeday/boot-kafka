package com.kafka.bootkafka.other;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public class CustomConsumerDefineOffSet {

    //自定义存储offset偏移量
    private static Map<TopicPartition, Long> currentOffset = new HashMap<>();

    public static void main(String[] args) {

        //创建配置信息
        Properties props = new Properties();

        //Kafka 集群
        props.put("bootstrap.servers", "127.0.0.1:9092");

        //消费者组，只要 group.id 相同，就属于同一个消费者组
        props.put("group.id", "test");

        //关闭自动提交 offset
        props.put("enable.auto.commit", "false");

        //Key 和 Value 的反序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //创建一个消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        //消费者订阅主题
        consumer.subscribe(Arrays.asList("first"), new ConsumerRebalanceListener() {

            //该方法会在 Rebalance 之前调用
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

                commitOffset(currentOffset);

            }

            //该方法会在 Rebalance 之后调用
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                currentOffset.clear();
                for (TopicPartition partition : partitions) {
                    consumer.seek(partition, getOffset(partition));//定位到最近提交的 offset 位置继续消费

                }
            }

        });
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));//消费者拉取数据
            for (ConsumerRecord<String, String> record : records) {
                TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
                currentOffset.put(topicPartition, record.offset());
            }
            commitOffset(currentOffset);//异步提交
        }
    }

    //获取某分区的最新 offset
    private static long getOffset(TopicPartition partition) {
        return 0;
    }

    //提交该消费者所有分区的 offset
    private static void commitOffset(Map<TopicPartition, Long> currentOffset) {
        if(currentOffset!=null){
            currentOffset.forEach((topicPartition, offset) -> {
                System.out.println("主题"+topicPartition.topic()+"----------"+"分区"+topicPartition.partition()+"offset"+offset);

            });

        }
    }
}
