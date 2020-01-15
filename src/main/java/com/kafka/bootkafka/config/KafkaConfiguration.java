package com.kafka.bootkafka.config;

import com.kafka.bootkafka.customer.KafkaRecordFilterStrategy;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka是分布式的发布—订阅消息系统。它最初由LinkedIn(领英)公司发布，使用Scala和Java语言编写，与2010年12月份开源，成为Apache的顶级项目。Kafka是一个高吞吐量的、持久性的、分布式发布订阅消息系统。
 *
 * 三大特点：
 *
 * 　　高吞吐量 可以满足每秒百万级别消息的生产和消费——生产消费。
 *
 * 　　持久性 有一套完善的消息存储机制，确保数据的高效安全的持久化——中间存储。
 *
 * 　　分布式 基于分布式的扩展和容错机制；Kafka的数据都会复制到几台服务器上。当某一台故障失效时，生产者和消费者转而使用其它的机器——整体健壮性。
 *
 * 核心：
 *
 * 　　Broker:消息代理，一个Kafka节点就是一个broker，多个broker可以组成一个Kafka集群。
 *
 * 　　Topic:主题，Kafka处理的消息的不同分类。
 *
 * 　　Partition:Topic物理上的分组，一个topic可以分为多个partion,每个partion是一个有序的队列。partion中每条消息都会被分配一个有序的Id(offset)
 *
 * 　　Message:消息，是通信的基本单位，每个producer可以向一个topic（主题）发布一些消息，每个消息都属于一个partition
 *
 * 　　Producer：消息和数据的生产者，向Kafka的一个topic发布消息。
 *
 * 　　Consumer：消息和数据的消费者，定于topic并处理其发布的消息。
 */
@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean autoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private Integer autoCommitInterval;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private Integer maxPollRecords;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.producer.retries}")
    private Integer retries;

    @Value("${spring.kafka.producer.batch-size}")
    private Integer batchSize;

    @Value("${spring.kafka.producer.buffer-memory}")
    private Integer bufferMemory;


    @Value("${spring.kafka.listener.poll-timeout}")
    private Long pollTimeout;

    @Value("${spring.kafka.listener.concurrencys}")
    private Integer concurrency;

    @Value("${spring.kafka.consumer.session-timeout}")
    private String sessionTimeout;

    @Value("${spring.kafka.listener.batch-listener}")
    private Boolean batchListener;

    @Value("${spring.kafka.consumer.max-poll-interval}")
    private Integer maxPollInterval;

    @Value("${spring.kafka.consumer.max-partition-fetch-bytes}")
    private Integer maxPartitionFetchBytes;

    @Autowired
    private KafkaRecordFilterStrategy kafkaRecordFilterStrategy;

    /**
     * 生产者配置信息
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        //producer端的消息确认机制,-1和all都表示消息不仅要写入本地的leader中还要写入对应的副本中
        props.put(ProducerConfig.ACKS_CONFIG, "0");//单个brok 推荐使用'1'
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries); //设置重试次数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);//达到batchSize大小的时候会发送消息
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);//延时时间，延时时间到达之后计算批量发送的大小没达到也发送消息
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory); //缓冲区的值
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);//序列化手段
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);//序列化手段
        //设置broker响应时间，如果broker在60秒之内还是没有返回给producer确认消息，则认为发送失败
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
        //指定拦截器(value为对应的class)
        //props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.te.handler.KafkaProducerInterceptor");
        //设置压缩算法(默认是木有压缩算法的)
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");//snappy
        return props;
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

    /**
     * 创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,没有此bean无法自定义的使用adminClient创建topic
     *
     * @return
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        //配置Kafka实例的连接地址
        //kafka的地址，不是zookeeper
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    /**
     * kafka客户端，在spring中创建这个bean之后可以注入并且创建topic,用于集群环境，创建对个副本
     *
     * @return
     */
    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }

    /**
     * 生产者工厂
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 生产者模板
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * 此模板已经设置了topic的名称，使用的时候可以直接注入此bean然后调用setDefaultTopic方法
     *
     * @return
     */
    @Bean("defaultKafkaTemplate")
    public KafkaTemplate<Integer, String> defaultKafkaTemplate() {
        KafkaTemplate template = new KafkaTemplate<String, String>(producerFactory());
        template.setDefaultTopic("topic.quick.default");
        return template;
    }

    /**
     * 消费者配置信息
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, maxPollInterval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * 消费者批量工程
     */
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(batchListener);

        //设置拉取等待时间(也可间接的理解为延时消费)
        factory.getContainerProperties().setPollTimeout(pollTimeout);

        //如果消息队列中没有消息，等待timeout毫秒后，调用poll()方法。
        // 如果队列中有消息，立即消费消息，每次消费的消息的多少可以通过max.poll.records配置。
        //手动提交无需配置
        //设置提交偏移量的方式， MANUAL_IMMEDIATE 表示消费一条提交一次；MANUAL表示批量提交一次 ENABLE_AUTO_COMMIT_CONFIG为 false
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        //设置并发量，小于或等于Topic的分区数,并且要在consumerFactory设置一次拉取的数量
        factory.setConcurrency(concurrency);

        //设置回复模板，类似于rabbitMQ的死信交换机，但是还有区别,
        //   factory.setReplyTemplate(kafkaTemplate());//发送消息的模板，这里只是消费者的类，所以木有

        //禁止自动启动,用于持久化操作，可先将消息都发送至broker，然后在固定的时间内进行持久化，有丢失消息的风险
        factory.setAutoStartup(false);

        //使用过滤器
        //配合RecordFilterStrategy使用，被过滤的信息将被丢弃
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(kafkaRecordFilterStrategy);

        return factory;
    }

    /**
     * 并发数
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "batchFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaListenerContainerFactory();
        factory.setConcurrency(concurrency);
        return factory;
    }


}
