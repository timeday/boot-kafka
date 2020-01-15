package com.kafka.bootkafka;

import com.alibaba.fastjson.JSON;
import com.kafka.bootkafka.common.Contants;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 参考文章：
 * https://segmentfault.com/a/1190000021405060?utm_source=tag-newest
 */
@EnableScheduling
@SpringBootApplication
public class BootKafkaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BootKafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JSON.DEFFAULT_DATE_FORMAT= Contants.DateTimeFormat.DATE_TIME_PATTERN;
    }
}
