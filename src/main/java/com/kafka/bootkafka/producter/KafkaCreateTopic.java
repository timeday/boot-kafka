package com.kafka.bootkafka.producter;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 创建主题
 */
public class KafkaCreateTopic {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put("bootstrap.servers","127.0.0.1:9092");

        AdminClient admin = AdminClient.create(prop);

        ArrayList<NewTopic> topics = new ArrayList<NewTopic>();
        // 创建主题  参数：主题名称、分区数、副本数
        NewTopic newTopic = new NewTopic("topic4", 1, (short)1);
        topics.add(newTopic);

        CreateTopicsResult result = admin.createTopics(topics);

        try {
            result.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
