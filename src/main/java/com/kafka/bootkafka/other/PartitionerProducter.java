package com.kafka.bootkafka.other;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class PartitionerProducter {

    public static void main(String[] args) throws ExecutionException,InterruptedException {

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");//kafka 集群

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

        Producer<String, String> producer = new
                KafkaProducer<String, String>(props);
        for (int i = 0; i < 100; i++) {

            producer.send(new ProducerRecord<String, String>("test",
                    Integer.toString(i), Integer.toString(i)), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("success->" +metadata.offset() +"--"+metadata.partition());
                    } else {
                        exception.printStackTrace();
                    }
                }
            });
        }
        producer.close();
    }

}
