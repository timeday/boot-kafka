package com.kafka.bootkafka.producter;


import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;

/**
 * Spring Kafka相关的注解有如下几个：
 *
 * 注解类型	描述
 * EnableKafka	启用由AbstractListenerContainerFactory在封面(covers)下创建的Kafka监听器注解端点，用于配置类；
 * EnableKafkaStreams	启用默认的Kafka流组件
 * KafkaHandler	在用KafkaListener注解的类中，将方法标记为Kafka消息监听器的目标的注解
 * KafkaListener	将方法标记为指定主题上Kafka消息监听器的目标的注解
 * KafkaListeners	聚合多个KafkaListener注解的容器注解
 * PartitionOffset	用于向KafkaListener添加分区/初始偏移信息
 * TopicPartition	用于向KafkaListener添加主题/分区信息
 * kafka的回调类，可以在此类中定义producer发送消息失败时候的回调方法
 */
@Service
public class KafkaSendResultHandler implements ProducerListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaSendResultHandler.class);

	@Override
	public void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
		// TODO Auto-generated method stub
		log.info("消息发送成功");
	}

	@Override
	public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
		// TODO Auto-generated method stub
		//可重试
		System.out.println("消息发送失败");
		
	}

	@Override
	public boolean isInterestedInSuccess() {
		// TODO Auto-generated method stub
		return false;
	}

}
