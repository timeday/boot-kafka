package com.kafka.bootkafka.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kafka.bootkafka.producter.KafkaProducer;
import com.kafka.bootkafka.producter.KafkaSendResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserTask {

    @Value("${topicName.topic2}")
    private String topics;

    @Autowired
    private KafkaProducer kafkaProducer;


    @Scheduled(fixedRate = 10 * 1000)
    public void addUserTask() {
        User user = new User();
        user.setUserName("HS");
        user.setDescription("text");
        user.setCreateTime(LocalDateTime.now());
        String JSONUser = JSON.toJSONStringWithDateFormat(user,
                Contants.DateTimeFormat.DATE_TIME_PATTERN,//日期格式化
                SerializerFeature.PrettyFormat);//格式化json
        for (int i = 0; i < 700; i++) {
            kafkaProducer.send(topics, JSONUser);
        }
    }
}



