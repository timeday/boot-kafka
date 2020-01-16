package com.kafka.bootkafka.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kafka.bootkafka.producter.KafkaProducer;
import com.kafka.bootkafka.producter.KafkaSendResultHandler;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class UserTask {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private AdminClient adminClient;


    @Scheduled(fixedRate = 10 * 1000)
    public void addUserTask() throws Exception {
        User user = new User();
        user.setUserName("HS");
        user.setDescription("text");
        user.setCreateTime(LocalDateTime.now());
        String JSONUser = JSON.toJSONStringWithDateFormat(user, Contants.DateTimeFormat.DATE_TIME_PATTERN,//日期格式化
                SerializerFeature.PrettyFormat);//格式化json
        //获取所有的主题数据
        /*ListTopicsResult listTopicsResult = adminClient.listTopics();
        KafkaFuture<Set<String>> future = listTopicsResult.names();
        for (int i = 0; i < 700; i++) {
            future.get().forEach(n -> kafkaProducer.send(n, JSONUser));

        }*/
        for (int i = 0; i < 700; i++) {
            kafkaProducer.send("topic5", JSONUser);
        }
    }
}
