package com.kafka.bootkafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//如果topic需要初始化：可以配置
@Configuration
public class KafkaInitialConfiguration {

    /**
     * 创建TopicName为topic.quick.initial的Topic并设置分区数为8以及副本数为1
     * @return
     */
    @Bean//通过bean创建(bean的名字为initialTopic)
    public NewTopic initialTopic() {
        return new NewTopic("topic.quick.initial", 1, (short) 1);
    }

    /**
     * 此种@Bean的方式，如果topic的名字相同，那么会覆盖以前的那个
     * 修改后|分区数量会变成11个 注意分区数量只能增加不能减少
     * @return
     */
    @Bean
    public NewTopic initialTopic2() {
        return new NewTopic("topic.quick.initial", 2, (short) 1);
    }

    /**
     * 配置主题
     * 要在应用启动时就创建主题，可以添加NewTopic类型的Bean。如果该主题已经存在，则忽略Bean。
     * @return
     */
    @Bean(name = "topic3")
    public NewTopic topic3() {
        return new NewTopic("topic3", 2, (short) 1);
    }

    @Bean(name = "topic4")
    public NewTopic topic4() {
        return new NewTopic("topic4", 2, (short) 1);
    }

    @Bean(name = "topic1")
    public NewTopic topic1() {
        return new NewTopic("topic1", 2, (short) 1);
    }

    @Bean(name = "topic2")
    public NewTopic topic2() {
        return new NewTopic("topic2", 2, (short) 1);
    }

    @Bean(name = "topic5")
    public NewTopic topic5() {
        return new NewTopic("topic5", 2, (short) 1);
    }
}