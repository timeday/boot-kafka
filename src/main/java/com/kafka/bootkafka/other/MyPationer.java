package com.kafka.bootkafka.other;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

//自定义分区
public class MyPationer implements Partitioner {

    public int partition(String s, Object key, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        Integer integer = cluster.partitionCountForTopic(s);
        return key.toString().hashCode() % integer;
    }

    public void close() {

    }

    public void configure(Map<String, ?> map) {

    }
}
