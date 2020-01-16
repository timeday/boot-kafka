package com.kafka.bootkafka.producter;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 删除主题
 */
public class KafkaDeleteTopic {
    public static void main(String[] args) {

        Properties prop = new Properties();
        prop.put("bootstrap.servers", "127.0.0.1:9092");
        AdminClient client = AdminClient.create(prop);

        ArrayList<String> topics = new ArrayList<>();
        topics.add("topic3");
        topics.add("topic4");

        DeleteTopicsResult result = client.deleteTopics(topics);

        try {
            result.all().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
