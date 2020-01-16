package com.kafka.bootkafka.producter;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 主题列表
 */
public class KafkaTopicList {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "127.0.0.1:9092");
        AdminClient admin = AdminClient.create(prop);

        ListTopicsResult result = admin.listTopics();
        KafkaFuture<Set<String>> future = result.names();

        try {
            System.out.println("==================Kafka Topics====================");
            future.get().forEach(n -> System.out.println(n));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
