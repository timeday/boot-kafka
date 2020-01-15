package com.kafka.bootkafka.other;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * 增加时间戳拦截器
 * 主要用于实现 clients 端的定
 * 制化控制逻辑。
 * 对于 producer 而言，interceptor 使得用户在消息发送前以及 producer 回调逻辑前有机会
 * 对消息做一些定制化需求，比如修改消息等。同时，producer 允许用户指定多个 interceptor
 * 按序作用于同一条消息从而形成一个拦截链(interceptor chain)。Intercetpor 的实现接口是
 * org.apache.kafka.clients.producer.ProducerInterceptor
 */
public class TimeInterceptor implements ProducerInterceptor<String, String> {


    /**
     * 获取配置信息和初始化数据时调用
     *
     * @param configs
     */
    @Override
    public void configure(Map<String, ?> configs) {
    }

    /**
     * 该方法封装进 KafkaProducer.send 方法中，即它运行在用户主线程中。Producer 确保在
     * 消息被序列化以及计算分区前调用该方法。用户可以在该方法中对消息做任何操作，但最好
     * 保证不要修改消息所属的 topic 和分区，否则会影响目标分区的计算
     *
     * @param record
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        // 创建一个新的 record，把时间戳写入消息体的最前部
        return new ProducerRecord(record.topic(),
                record.partition(),
                record.timestamp(),
                record.key(),
                System.currentTimeMillis() + "," +
                        record.value().toString());
    }

    /**
     * 该方法会在消息被应答或消息发送失败时调用，并且通常都是在 producer 回调逻辑触发之前。
     * onAcknowledgement 运行在 producer 的 IO 线程中，因此不要在该方法中放入很重
     * 的逻辑，否则会拖慢 producer 的消息发送效率
     * @param metadata
     * @param exception
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata,
                                  Exception exception) {
    }

    /**
     * 关闭 interceptor，主要用于执行一些资源清理工作
     * 如前所述，interceptor 可能被运行在多个线程中，因此在具体实现时用户需要自行确保
     * 线程安全。另外倘若指定了多个 interceptor，则 producer 将按照指定顺序调用它们，并仅仅
     * 是捕获每个 interceptor 可能抛出的异常记录到错误日志中而非在向上传递。这在使用过程中
     * 要特别留意
     */
    @Override
    public void close() {
    }
}