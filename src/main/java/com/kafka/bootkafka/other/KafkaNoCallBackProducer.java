package com.kafka.bootkafka.other;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;


public class KafkaNoCallBackProducer {

    public static void main(String[] args){

        Properties props = new Properties();
        //kafka 集群，broker-list
        props.put("bootstrap.servers", "127.0.0.1:9092");

        props.put("acks", "all");
        //重试次数
        props.put("retries", 1);
       //批次大小
        props.put("batch.size", 16384);
         //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 100; i++) {
            //TOPIC 需要向创建test
            // kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
            producer.send(new ProducerRecord<String, String>("test",
                    Integer.toString(i), Integer.toString(i)));
        }
        producer.close();
    }
}
