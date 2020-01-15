package com.kafka.bootkafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @EnableKafka并不是在Spring Boot中启用Kafka必须的，Spring Boot附带了Spring Kafka的自动配置，因此不需要使用显式的@EnableKafka。如果想要自己实现Kafka配置类，则需要加上@EnableKafka，如果你不想要Kafka自动配置，比如测试中，需要做的只是移除KafkaAutoConfiguration：
 * @SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
 */
@SpringBootTest
class BootKafkaApplicationTests {

    @Autowired // adminClien需要自己生成配置bean
    private AdminClient adminClient;


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private KafkaTemplate defaultKafkaTemplate;


    @Test//自定义手动创建topic和分区
    public void testCreateTopic() throws InterruptedException {
        // 这种是手动创建 //10个分区，一个副本
        // 分区多的好处是能快速的处理并发量，但是也要根据机器的配置
        NewTopic topic = new NewTopic("topic.manual.create", 10, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        Thread.sleep(1000);
    }

    @Test
    public void testDefaultKafka(){
        //前提是要在创建模板类的时候指定topic，否则回报找不到topic
        defaultKafkaTemplate.setDefaultTopic("这里发送的消息");


    }
    /**
     * 获取所有的topic
     *
     * @throws Exception
     */
    @Test
    public void getAllTopic() throws Exception {
        ListTopicsResult listTopics = adminClient.listTopics();
        Set<String> topics = listTopics.names().get();

        for (String topic : topics) {
            System.err.println(topic);

        }
    }

    @Test // 遍历某个topic信息
    public void testSelectTopicInfo() throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("topic.quick.initial"));
        result.all().get().forEach((k, v) -> System.out.println("k: " + k + " ,v: " + v.toString() + "\n"));
    }

}
