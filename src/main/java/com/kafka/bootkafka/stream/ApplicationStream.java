package com.kafka.bootkafka.stream;


import java.util.Properties;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;

public class ApplicationStream {

    /**
     * 创建主题：kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic second
     * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic first
     * 创建生产者；kafka-console-producer.bat --broker-list localhost:9092 --topic first
     * 创建消费者：kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic second --from-beginning
     * @param args
     */
    public static void main(String[] args) {

        // 定义输入的topic
        String from = "first";

        // 定义输出的topic
        String to = "second";

        // 设置参数
        Properties settings = new Properties();

        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "logFilter");

        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        StreamsConfig config = new StreamsConfig(settings);

        // 构建拓扑
        Topology builder = new Topology();

        builder.addSource("SOURCE", from)
                .addProcessor("PROCESS", new ProcessorSupplier<byte[], byte[]>() {
                    @Override
                    public Processor<byte[], byte[]> get() {
                        // 具体分析处理
                        return new LogProcessor();
                    }
                }, "SOURCE")
                .addSink("SINK", to, "PROCESS");

        // 创建kafka stream
        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }
}
