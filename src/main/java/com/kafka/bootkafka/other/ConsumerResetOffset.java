package com.kafka.bootkafka.other;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 从任意位置开始消费
 */
public class ConsumerResetOffset {


    public static void main(String[] args) {

        String topicName = "first";

        Properties props = new Properties();

        props.put("bootstrap.servers", "127.0.0.1:9092");

        /**
         * 订阅一个topic之前要设置从这个topic的offset为0的地方获取。
         *   注意：这样的方法要保证这个group.id是新加入，如果是以前存在的，那么会抛异常。
         */
        props.put("group.id", "test1");

        props.put("enable.auto.commit", "true");

        props.put("auto.commit.interval.ms", "1000");

        props.put("session.timeout.ms", "30000");

        //要发送自定义对象，需要指定对象的反序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, Object> consumer = new KafkaConsumer<String, Object>(props);

        //从该topic开始位置消费 开始位置是初始位置零
        Map<TopicPartition, OffsetAndMetadata> hashMaps = new HashMap<TopicPartition, OffsetAndMetadata>();
        hashMaps.put(new TopicPartition(topicName, 0), new OffsetAndMetadata(10));
        //同步
        consumer.commitSync(hashMaps);

        consumer.subscribe(Arrays.asList(topicName));

        /**
         * 获取指定的topic任意的offset
         */
        //来分配topic和partition
//        consumer.assign(Arrays.asList(new TopicPartition(topicName, 0)));
//        //不改变当前offset 从offset为10开始消费
//        consumer.seek(new TopicPartition(topicName, 0), 10);

        while (true) {

            ConsumerRecords<String, Object> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, Object> record : records){

                System.out.println("测试---"+record.toString());

            }
        }

    }

}
