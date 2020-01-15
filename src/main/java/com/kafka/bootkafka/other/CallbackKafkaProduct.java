package com.kafka.bootkafka.other;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 回调函数会在 producer 收到 ack 时调用，为异步调用，该方法有两个参数，分别是
 * RecordMetadata 和 Exception，如果 Exception 为 null，说明消息发送成功，如果
 * Exception 不为 null，说明消息发送失败。
 * 注意：消息发送失败会自动重试，不需要我们在回调函数中手动重试。
 */
public class CallbackKafkaProduct {

    public static void main(String[] args) throws ExecutionException,
            InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092");//kafka 集
        props.put("acks", "all");
        props.put("retries", 1);//重试次数
        props.put("batch.size", 16384);//批次大小
        props.put("linger.ms", 1);//等待时间
        props.put("buffer.memory", 33554432);//RecordAccumulator 缓
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        //添加自定义分区器
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.kafka.bootkafka.other.MyPationer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        for (int i = 0; i < 100; i++) {
            //TOPIC 需要向创建test
            // kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
            producer.send(new ProducerRecord<String, String>("test",
                    Integer.toString(i), Integer.toString(i)), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("success->" +
                                metadata.offset());
                    } else {
                        exception.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }
}